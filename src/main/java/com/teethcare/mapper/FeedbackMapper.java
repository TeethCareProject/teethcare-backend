package com.teethcare.mapper;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.request.ReportRequest;
import com.teethcare.model.response.*;
import org.mapstruct.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper(componentModel = "spring",  uses = {BookingMapper.class})
public interface FeedbackMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "booking", ignore = true)
    @Mapping(source = "bookingId", target = "id")
    Feedback mapFeedbackRequestToFeedback(FeedbackRequest feedbackRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    FeedbackResponse mapFeedbackToFeedbackResponse(Feedback feedback);

    @InheritConfiguration(name = "mapReportToReportResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "booking", target = "bookingResponse")
    @Mapping(source = "reports", target = "reports")
    FeedbackByClinicResponse mapFeedbackToFeedbackByClinicResponse(Feedback feedback);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<FeedbackResponse> mapFeedbackListToFeedbackResponseList(List<Feedback> feedbacks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "feedback", target = "feedbackResponse")
    @Mapping(source = "createdTime", target = "createdTime")
    ReportResponse mapReportToReportResponse(Report report);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "feedback", target = "feedbackResponse")
    @Mapping(source = "createdTime", target = "createdTime")
    List<ReportResponse> mapReportListToReportResponseList(List<Report> report);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Report mapReportRequestToReport(ReportRequest reportRequest);

/*    static Long mapDateTimeToLong(Timestamp dateTime) {
        if (dateTime != null) {
            return  dateTime.getTime();
        } else {
            return null;
        }

    }*/

}
