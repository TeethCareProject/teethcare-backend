package com.teethcare.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teethcare.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.*;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String username;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private int gender;
    private String avatarImage;
    private Date dateOfBirth;
    private int status;
    private Collection<? extends  GrantedAuthority> authorities;

    public static UserDetailsImpl build(Account account){
        List<GrantedAuthority> authorityList= new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(account.getRole().getName()));
        UserDetailsImpl userDetails = new UserDetailsImpl(account.getUsername(),account.getPassword(), account.getFirstName(),
                account.getLastName(), account.getGender(), account.getAvatarImage(), account.getDateOfBirth(), account.getStatus(), authorityList);
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGender() {
        return gender;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
