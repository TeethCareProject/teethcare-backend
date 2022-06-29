package com.teethcare.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.*;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.config.security.UserDetailUtil;
import com.teethcare.config.security.UserDetailsImpl;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.InternalServerError;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.*;
import com.teethcare.model.response.*;
import com.teethcare.service.*;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.mail.MessagingException;
import javax.management.BadAttributeValueExpException;
import javax.validation.Valid;
import java.util.List;

import java.util.List;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Booking.BOOKING_ENDPOINT)
@Slf4j
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

    @PostMapping(path = "/create-from-appointment")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<Object> bookingService(@Valid @RequestBody BookingFromAppointmentRequest bookingFromAppointmentRequest,
                                                 @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);

        Account account = accountService.getAccountByUsername(username);

        Booking booking = bookingService.saveBookingFromAppointment(bookingFromAppointmentRequest, account);
        try {
            if (booking != null) {
                PatientBookingResponse patientBookingResponse = bookingMapper.mapBookingToPatientBookingResponse(booking);
                patientBookingResponse.setDesiredCheckingTime(booking.getDesiredCheckingTime().getTime());
                if (bookingFromAppointmentRequest.getServiceId() != null) {
                    ServiceOfClinic service = serviceOfClinicService.findById(bookingFromAppointmentRequest.getServiceId());
                    patientBookingResponse.setServiceName(service.getName());
                }
                firebaseMessagingService.sendNotification(booking.getPreBooking().getPreBooking().getId(), NotificationType.CREATE_BOOKING_SUCCESS.name(),
                        NotificationMessage.CREATE_BOOKING_SUCCESS + booking.getId(), Role.DENTIST.name());

                return new ResponseEntity<>(patientBookingResponse, HttpStatus.OK);
            }
        } catch (FirebaseMessagingException |
                 BadAttributeValueExpException e) {
            return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Message.CREATE_FAIL, HttpStatus.BAD_REQUEST);
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

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE, T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<MessageResponse> isAccepted(@RequestBody ObjectNode objectNode,
                                                      @PathVariable(value = "id") int bookingId,
                                                      @RequestHeader(AUTHORIZATION) String token) {
        String username = UserDetailUtil.getUsername();
        String role = UserDetailUtil.getRole();

        switch (Role.valueOf(role)) {
            case CUSTOMER_SERVICE:
                Account account = accountService.getAccountByUsername(username);
                CustomerService customerService = CSService.findById(account.getId());

                boolean isAccepted = bookingService.confirmBookingRequest(bookingId, customerService, objectNode);
                String rejectedNote = bookingService.findBookingById(bookingId).getRejectedNote();
                if (!isAccepted) {
                    try {
                        Booking booking = bookingService.findBookingById(bookingId);
                        firebaseMessagingService.sendNotification(bookingId, NotificationType.REJECT_BOOKING.name(),
                                NotificationMessage.REJECT_BOOKING + rejectedNote, Role.PATIENT.name());
                        emailService.sendRejectBooking(booking);
                    } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                        return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.INTERNAL_SERVER_ERROR);
                    } catch (MessagingException e) {
                        return new ResponseEntity<>(new MessageResponse(Message.ERROR_SENDMAIL.name()), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                break;
            case PATIENT:
                bookingService.rejectBookingRequest(bookingId);
                break;
        }

        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

    @PutMapping("/first-update")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody BookingUpdateRequest bookingUpdateRequest,
                                                  @RequestParam(value = "isAllDeleted", required = false, defaultValue = "false") boolean isAllDeleted) {

        int bookingId = bookingUpdateRequest.getBookingId();

        Booking booking = bookingService.findBookingById(bookingId);
        int patientId = booking.getPatient().getId();
        String status = booking.getStatus();

        if (Status.Booking.valueOf(status) == Status.Booking.REQUEST) {
            bookingService.firstlyUpdated(bookingUpdateRequest, isAllDeleted);

            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.UPDATE_BOOKING_1ST_NOTIFICATION.name(),
                        NotificationMessage.UPDATE_1ST_MESSAGE + bookingId, Role.PATIENT.name());
                firebaseMessagingService.sendNotification(bookingId, NotificationType.UPDATE_BOOKING_1ST_NOTIFICATION.name(),
                        NotificationMessage.UPDATE_1ST_MESSAGE + bookingId, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/second-update")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).DENTIST)")
    public ResponseEntity<MessageResponse> secondlyUpdated(@Valid @RequestBody BookingUpdateRequest bookingUpdateRequest,
                                                           @RequestParam(value = "isAllDeleted", required = false, defaultValue = "false") boolean isAllDeleted) {
        boolean isSuccess;

        int bookingId = bookingUpdateRequest.getBookingId();

        Booking booking = bookingService.findBookingById(bookingId);
        String status = booking.getStatus();

        if (Status.Booking.valueOf(status) == Status.Booking.TREATMENT) {
            isSuccess = bookingService.secondlyUpdated(bookingUpdateRequest, isAllDeleted);

            if (!isSuccess) {
                log.warn("Fail for uncompleted service");
                return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.BAD_REQUEST);
            }

            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.UPDATE_BOOKING_2RD_NOTIFICATION.name(),
                        NotificationMessage.UPDATE_2RD_MESSAGE + bookingId, Role.PATIENT.name());
                log.info("Successful notification");
                emailService.sendBookingConfirmEmail(booking);
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (MessagingException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SENDMAIL.name()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            log.warn("Fail for wrong status");
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/confirm")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<MessageResponse> confirmFinalBooking(@Valid @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        boolean isUpdated = bookingService.confirmFinalBooking(bookingUpdateRequest);
        int bookingId = bookingUpdateRequest.getBookingId();

        if (isUpdated) {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CONFIRM_BOOKING_SUCCESS.name(),
                        NotificationMessage.CONFIRM_BOOKING_SUCCESS + bookingId, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CONFIRM_BOOKING_FAIL.name(),
                        NotificationMessage.CONFIRM_BOOKING_FAIL + bookingId, Role.DENTIST.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                return new ResponseEntity<>(new MessageResponse(Message.ERROR_SEND_NOTIFICATION.name()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new MessageResponse(Message.UPDATE_FAIL.name()), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/checkin/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> checkin(@PathVariable(value = "id") String id) {
        int bookingId;
        try {
            bookingId = Integer.parseInt(id);
        } catch(NumberFormatException exception) {
            bookingId = 0;
        }
        boolean isCheckin = true;
        boolean isUpdated = bookingService.updateStatus(bookingId, isCheckin);

        if (isUpdated) {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CHECK_IN_SUCCESS.name(),
                        NotificationMessage.CHECK_IN_SUCCESS, Role.PATIENT.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                throw new InternalServerError("Failed for send mail");
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            throw new BadRequestException("Not reach time to check in");
        }
    }

    @PutMapping("/checkout/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> checkout(@PathVariable(value = "id") String id) {
        int bookingId;
        try {
            bookingId = Integer.parseInt(id);
        } catch(NumberFormatException exception) {
            bookingId = 0;
        }
        boolean isCheckin = false;
        boolean isUpdated = bookingService.updateStatus(bookingId, isCheckin);
        if (isUpdated) {
            try {
                firebaseMessagingService.sendNotification(bookingId, NotificationType.CHECK_OUT_SUCCESS.name(),
                        NotificationMessage.CHECK_OUT_SUCCESS, Role.PATIENT.name());
            } catch (FirebaseMessagingException | BadAttributeValueExpException e) {
                throw new InternalServerError("Failed for send mail");
            }
            return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
        } else {
            throw new BadRequestException("Not reach time to check out");
        }
    }

    @GetMapping("/check-available-time")
    public ResponseEntity<CheckAvailableTimeResponse> checkAvailableTime(@Valid CheckAvailableTimeRequest checkAvailableTimeRequest) {
        boolean result = bookingService.checkAvailableTime(checkAvailableTimeRequest);
        CheckAvailableTimeResponse checkAvailableTimeResponse = new CheckAvailableTimeResponse(Status.CheckTime.UNAVAILABLE.name());
        if (result) {
            checkAvailableTimeResponse.setStatus(Status.CheckTime.AVAILABLE.name());
            return new ResponseEntity<>(checkAvailableTimeResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(checkAvailableTimeResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-available-time")
    public ResponseEntity<GetAvailableTimeResponse> getAvailableTimeByDate(@Valid GetAvailableTimeRequest getAvailableTimeRequest) {
        List<Integer> result = bookingService.getAvailableTime(getAvailableTimeRequest);
        GetAvailableTimeResponse getAvailableTimeResponse = new GetAvailableTimeResponse(result);
        return new ResponseEntity<>(getAvailableTimeResponse, HttpStatus.OK);
    }
}
