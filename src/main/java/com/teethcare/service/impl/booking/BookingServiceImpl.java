package com.teethcare.service.impl.booking;

import com.teethcare.common.Message;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.request.BookingUpdateRequest;
import com.teethcare.model.response.PatientBookingResponse;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.PatientRepository;
import com.teethcare.service.*;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ServiceOfClinicService serviceOfClinicService;
    private final PatientService patientService;
    private final DentistService dentistService;
    private final ClinicService clinicService;


    @Override
    public List<Booking> findAll() {
        //TODO
        return null;
    }

    @Override
    public Booking findById(int id) {
        return bookingRepository.getById(id);
    }

    @Override
    public void save(Booking entity) {
        bookingRepository.save(entity);
    }

    @Override
    public void delete(int id) {
        Booking booking = findById(id);
        booking.setStatus(Status.Booking.UNAVAILABLE.name());
        save(booking);
    }

    @Override
    public void update(Booking entity) {
        //TODO
    }

    @Override
    public Booking saveBooking(BookingRequest bookingRequest, Account account) {
        Booking bookingTmp = bookingMapper.mapBookingRequestToBooking(bookingRequest);
        //get millisecond
        long millisecond = bookingRequest.getDesiredCheckingTime();

        Timestamp desiredCheckingTime = ConvertUtils.getTimestamp(millisecond);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (desiredCheckingTime.compareTo(now) < 0){
            throw new BadRequestException("Desired checking time invalid");
        }
        bookingTmp.setDesiredCheckingTime(desiredCheckingTime);

        //set service to booking
        int serviceID = bookingRequest.getServiceId();
        ServiceOfClinic service = serviceOfClinicService.findById(serviceID);
        List<ServiceOfClinic> serviceOfClinicList = new ArrayList<>();
        serviceOfClinicList.add(service);
        bookingTmp.setServices(serviceOfClinicList);

        //set clinic to booking
        Clinic clinic = service.getClinic();
        bookingTmp.setClinic(clinic);

        //set patient to booking
        Patient patient = patientService.findById(account.getId());
        bookingTmp.setPatient(patient);
        bookingTmp.setStatus(Status.Booking.PENDING.name());

        if (patient != null && !serviceOfClinicList.isEmpty() && clinic != null){
            return bookingRepository.save(bookingTmp);
        }
        return null;

    }

    @Override
    public Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable) {
        return bookingRepository.findAll(bookingSpecification, pageable);
    }

    @Override
    public Page<Booking> findAll(String role, int accountId,
                                 BookingFilterRequest filterRequest,
                                 Pageable pageable) {

        Page<Booking> bookingPage = null;

        switch (Role.valueOf(role)) {
            case CUSTOMER_SERVICE:
                Clinic clinic = clinicService.findClinicByCustomerServiceId(accountId);
                List<Booking> bookingListForCustomerService = bookingRepository.findBookingByClinic(clinic);

                bookingListForCustomerService = bookingListForCustomerService.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());
                return PaginationAndSortFactory.convertToPage(bookingListForCustomerService, pageable);
            case PATIENT:
                List<Booking> bookingListForPatient = bookingRepository.findBookingByPatientId(accountId);

                bookingListForPatient = bookingListForPatient.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

              return PaginationAndSortFactory.convertToPage(bookingListForPatient, pageable);
            case DENTIST:
                List<Booking> bookingListForDentist = bookingRepository.findBookingByDentistId(accountId);

                bookingListForDentist = bookingListForDentist.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

                return PaginationAndSortFactory.convertToPage(bookingListForDentist, pageable);
        }
        return null;
    }

    @Override
    @Transactional
    public void confirmBookingRequest(int bookingId, boolean isAccepted, CustomerService customerService) {
        Booking booking = findBookingById(bookingId);

        if (isAccepted) {
            booking.setStatus(Status.Booking.REQUEST.name());
        } else {
            booking.setStatus(Status.Booking.REJECTED.name());
        }
        booking.setCustomerService(customerService);

        save(booking);

    }

    @Override
    public boolean updateStatus(int bookingId) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        String status = booking.getStatus();
         switch (Status.Booking.valueOf(status)) {
             case REQUEST:
                 booking.setStatus(Status.Booking.TREATMENT.name());
                 break;
             case TREATMENT:
                 booking.setStatus(Status.Booking.DONE.name());
                 break;
             default:
                 return false;
         }

         return true;
    }

    @Override
    @Transactional
    public boolean update(BookingUpdateRequest bookingUpdateRequest) {
        int bookingId = bookingUpdateRequest.getBookingId();
        List<Integer> servicesIds = bookingUpdateRequest.getServiceIds();

        Booking booking = bookingRepository.findBookingById(bookingId);

        List<ServiceOfClinic> services = new ArrayList<>();
        for (Integer servicesId : servicesIds) {
            services.add(serviceOfClinicService.findById(servicesId));
        }
        String status = booking.getStatus();

        switch (Status.Booking.valueOf(status)) {
            case REQUEST:
                Integer dentistId = bookingUpdateRequest.getDentistId();
                Long examinationTimeRequest = bookingUpdateRequest.getExaminationTime();
                if (dentistId == null || examinationTimeRequest == null) {
                    throw new BadRequestException(Message.PARAMS_MISSING.name());
                }
                Dentist dentist = dentistService.findActive(dentistId);
                Timestamp examinationTime = ConvertUtils.getTimestamp(examinationTimeRequest);
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                if (examinationTime.compareTo(currentTime) < 0){
                    throw new BadRequestException(Message.DATE_ERROR.name());
                }

                List<Booking> checkedBookings = bookingRepository.findBookingByDentistIdAndStatus(dentistId, Status.Booking.TREATMENT.name());
                if (checkedBookings != null && !checkedBookings.isEmpty()) {
//                    System.out.println("Dentist id: " + dentistId + " has treated booking" + checkedBookings.get(0).getId());
                    throw new BadRequestException(Message.DENTIST_NO_AVAILABLE.name());
                }
//                System.out.println("Dentist id: " + dentistId + " has treated booking" + checkedBookings.get(0).getId());

                booking.setServices(services);
                booking.setExaminationTime(examinationTime);
                booking.setCreateBookingDate(currentTime);
                booking.setDentist(dentist);
                break;
            case TREATMENT:
                booking.setServices(services);
                break;
            default:
                return false;
        }

        save(booking);

        return true;
    }


    @Override
    public List<Booking> findAllByCustomerService(CustomerService customerService) {
        return bookingRepository.findAllByCustomerService(customerService);
    }

    @Override
    public Booking findBookingById(int id) {
        return bookingRepository.findBookingById(id);
    }
}
