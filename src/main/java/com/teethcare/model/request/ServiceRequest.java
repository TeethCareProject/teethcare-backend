package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ServiceRequest {
    @NotBlank
    @Length(max = 100)
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    private Integer duration;

    private String imageUrl;
}
