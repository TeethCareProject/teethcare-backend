package com.teethcare.mapper;

import com.teethcare.model.entity.Feedback;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.request.ReportRequest;
import com.teethcare.model.response.FeedbackByClinicResponse;
import com.teethcare.model.response.FeedbackResponse;
import com.teethcare.model.response.ReportResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "booking", ignore = true)
    @Mapping(source = "bookingID", target = "id")
    Feedback mapFeedbackRequestToFeedback(FeedbackRequest feedbackRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "booking.id", target = "bookingID")
    @Mapping(source = "booking.patient", target = "patientResponse")
    @Mapping(source = "booking.clinic", target = "clinicInfoResponse")
    FeedbackResponse mapFeedbackToFeedbackResponse(Feedback feedback);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "booking.id", target = "bookingID")
    @Mapping(source = "booking.patient", target = "patientResponse")
    FeedbackByClinicResponse mapFeedbackToFeedbackByClinicResponse(Feedback feedback);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<FeedbackResponse> mapFeedbackListToFeedbackResponseList(List<Feedback> feedbacks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "feedback", target = "feedbackResponse")
    ReportResponse mapReportToReportResponse(Report report);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Report mapReportRequestToReport(ReportRequest reportRequest);

}
