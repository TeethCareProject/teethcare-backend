package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private int id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FeedbackResponse feedbackResponse;
    private String detail;
    private String status;
    private Long createdTime;
}
