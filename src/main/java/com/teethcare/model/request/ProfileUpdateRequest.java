package com.teethcare.model.request;

import com.teethcare.utils.Trimmable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Getter
@Setter
public class ProfileUpdateRequest implements Trimmable {

    @Length(max = 40)
    private String firstName;

    @Length(max = 10)
    private String lastName;

    @Length(max = 10)
    private String gender;

    @Email
    @Length(max = 320)
    private String email;

    @Pattern(regexp = "(0)+(\\d){9}")
    private String phoneNumber;

    private String avatarImage;

    private long dateOfBirth;
}
