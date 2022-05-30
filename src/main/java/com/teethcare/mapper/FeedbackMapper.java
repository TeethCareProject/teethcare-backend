package com.teethcare.mapper;

import com.teethcare.model.entity.Feedback;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
    @Mapping(source = "booking.patient.firstName", target = "firstName")
    @Mapping(source = "booking.patient.lastName", target = "lastName")
    FeedbackResponse mapFeedbackToFeedbackResponse(Feedback feedback);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<FeedbackResponse> mapFeedbackListToFeedbackResponseList(List<Feedback> feedbacks);
}
