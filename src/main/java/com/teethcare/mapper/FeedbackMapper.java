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

@Mapper(componentModel = "spring",  uses = {BookingMapper.class, UserInforMapper.class},  config = ConfigurationMapper.class)

public interface FeedbackMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "booking", ignore = true)
    Feedback mapFeedbackRequestToFeedback(FeedbackRequest feedbackRequest);

    @Named(value = "mapFeedbackToFeedbackResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "booking.patient", target = "patient")
    @Mapping(source = "booking.id", target = "bookingId")
    FeedbackResponse mapFeedbackToFeedbackResponse(Feedback feedback);

    @InheritConfiguration(name = "mapReportToReportResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "booking", target = "bookingResponse")
    @Mapping(target = "reports", ignore = true)
    FeedbackByClinicResponse mapFeedbackToFeedbackByClinicResponse(Feedback feedback);

    @InheritConfiguration(name = "mapFeedbackToFeedbackByClinicResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<FeedbackByClinicResponse> mapFeedbackListToFeedbackByClinicResponseList(List<Feedback> feedback);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<FeedbackResponse> mapFeedbackListToFeedbackResponseList(List<Feedback> feedbacks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "createdTime", target = "createdTime")
    @Mapping(source = "feedback", target = "feedbackResponse",  qualifiedByName = "mapFeedbackToFeedbackResponse")
    ReportResponse mapReportToReportResponse(Report report);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Report mapReportRequestToReport(ReportRequest reportRequest);


}
