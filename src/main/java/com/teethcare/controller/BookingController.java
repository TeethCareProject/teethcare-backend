package com.teethcare.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.*;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.dto.BookingConfirmationDTO;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.request.BookingUpdateRequest;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.model.response.PatientBookingResponse;
import com.teethcare.service.*;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.management.BadAttributeValueExpException;
import javax.validation.Valid;

import static com.teethcare.common.Constant.EMAIL.BOOKING_DETAIL_CONFIRM;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Booking.BOOKING_ENDPOINT)
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final ServiceOfClinicService serviceOfClinicService;
    private final CSService CSService;
    private final AccountService accountService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final EmailService emailService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<PatientBookingResponse> bookingService(@Valid @RequestBody BookingRequest bookingRequest,
                                                                 @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);

        Account account = accountService.getAccountByUsername(username);

        Booking booking = bookingService.saveBooking(bookingRequest, account);
        PatientBookingResponse patientBookingResponse = bookingMapper.mapBookingToPatientBookingResponse(booking);
        patientBookingResponse.setDesiredCheckingTime(booking.getDesiredCheckingTime().getTime());

        ServiceOfClinic service = serviceOfClinicService.findById(bookingRequest.getServiceId());
        patientBookingResponse.setServiceName(service.getName());

        return new ResponseEntity<>(patientBookingResponse, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).PATIENT, " +
            "T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<Page<BookingResponse>> getAll(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                        @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction,
                                                        BookingFilterRequest requestFilter,
                                                        @RequestHeader(AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);

        Account account = accountService.getAccountByUsername(username);

        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);

        Page<Booking> bookingPage = bookingService.findAll(account.getRole().getName(), account.getId(), requestFilter, pageable);

        Page<BookingResponse> bookingResponsePage = bookingPage.map(bookingMapper::mapBookingToBookingResponse);

        return new ResponseEntity<>(bookingResponsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable("id") int id) {
        Booking booking = bookingService.findBookingById(id);
        BookingResponse bookingResponse = bookingMapper.mapBookingToBookingResponse(booking);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @PutMapping("/accept")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> isAccepted(@RequestParam(value = "isAccepted") boolean isAccepted,
                                                      @RequestParam(value = "bookingId") int bookingId,
                                                      @RequestHeader(AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);

        Account account = accountService.getAccountByUsername(username);

        CustomerService customerService = CSService.findById(account.getId());

        bookingService.confirmBookingRequest(bookingId, isAccepted, customerService);

        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

    @PutMapping("/first-update")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody BookingUpdateRequest bookingUpdateRequest,
                                                  @RequestParam(value = "isAllDeleted", required = false, defaultValue = "false") boolean isAllDeleted) {
        boolean isSuccess = false;

        int bookingId = bookingUpdateRequest.getBookingId();

        Booking booking = bookingService.findBookingById(bookingId);
        int patientId = booking.getPatient().getId();
        String status = booking.getStatus();

        if (Status.Booking.valueOf(status) == Status.Booking.REQUEST) {
            isSuccess = bookingService.firstlyUpdated(bookingUpdateRequest, isAllDeleted);

            if (!isSuccess) {
                return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
            }
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.UPDATE_BOOKING_1ST_NOTIFICATION.name(),
                        NotificationMessage.UPDATE_1ST_MESSAGE + bookingId, Role.PATIENT.name());
                firebaseMessagingService.sendNotification(bookingId, NotificationType.UPDATE_BOOKING_1ST_NOTIFICATION.name(),
                        NotificationMessage.UPDATE_1ST_MESSAGE + bookingId, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
        }
    }

    @PutMapping("/second-update")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).DENTIST)")
    public ResponseEntity<MessageResponse> secondlyUpdated(@Valid @RequestBody BookingUpdateRequest bookingUpdateRequest,
                                                  @RequestParam(value = "isAllDeleted", required = false, defaultValue = "false") boolean isAllDeleted) {
        boolean isSuccess = false;

        int bookingId = bookingUpdateRequest.getBookingId();

        Booking booking = bookingService.findBookingById(bookingId);
        int patientId = booking.getPatient().getId();
        int dentistId = booking.getDentist().getId();
        String status = booking.getStatus();

        if (Status.Booking.valueOf(status) == Status.Booking.TREATMENT) {
            isSuccess = bookingService.secondlyUpdated(bookingUpdateRequest, isAllDeleted);

            if (!isSuccess) {
                return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
            }

            try {
                firebaseMessagingService.sendNotification(bookingId,NotificationType.UPDATE_BOOKING_2RD_NOTIFICATION.name(),
                        NotificationMessage.UPDATE_2RD_MESSAGE + bookingId, Role.PATIENT.name());
                emailService.sendBookingConfirmEmail(booking);
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.OK);
            } catch (MessagingException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SENDMAIL.name()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
        }
    }

    @PutMapping("/confirm")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<MessageResponse> confirmFinalBooking(@Valid @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        boolean isUpdated = bookingService.confirmFinalBooking(bookingUpdateRequest);
        int bookingId = bookingUpdateRequest.getBookingId();
        Booking booking = bookingService.findBookingById(bookingId);
        if (isUpdated) {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CONFIRM_BOOKING_SUCCESS.name(),
                                                        NotificationMessage.CONFIRM_BOOKING_SUCCESS + bookingId, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            try {
//                int bookingId, String title, String body, String role
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CONFIRM_BOOKING_FAIL.name(),
                                                            NotificationMessage.CONFIRM_BOOKING_FAIL + bookingId, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
        }
    }


    @PutMapping("/checkin")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> checkin(@RequestParam(value = "bookingId") int bookingId) {
        boolean isUpdated = bookingService.updateStatus(bookingId);
        if (isUpdated) {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CHECK_OUT_SUCCESS.name(),
                        NotificationMessage.CHECK_OUT_SUCCESS, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
        }
    }

    @PutMapping("/checkout")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> checkout(@RequestParam(value = "bookingId") int bookingId) {
        boolean isUpdated = bookingService.updateStatus(bookingId);
        if (isUpdated) {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CHECK_OUT_SUCCESS.name(),
                        NotificationMessage.CHECK_OUT_SUCCESS, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.OK);
        }
    }
}
