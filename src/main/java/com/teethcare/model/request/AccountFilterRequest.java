package com.teethcare.model.request;

import com.teethcare.model.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class AccountFilterRequest {
    private String id;
    private String fullName;
    private String username;
    private String role;
    private String email;
    private String phone;
    private String status;

    public Predicate<Account> getPredicate() {
        Predicate<Account> predicate = (account -> true);
        if (fullName != null) {
            predicate = predicate.and((account) -> (account.getLastName() + " " + account.getFirstName()).toUpperCase()
                    .contains(fullName.replaceAll("\\s\\s+", " ").trim().toUpperCase()));
        }
        if (username != null) {
            predicate = predicate.and((account) -> (account.getUsername().toUpperCase()
                    .contains(username.trim().toUpperCase())));
        }
        if (status != null) {
            predicate = predicate.and((account) -> (account.getStatus()
                    .equalsIgnoreCase(status.trim())));
        }
        if (email != null) {
            predicate = predicate.and((account) -> (account.getEmail() != null && account.getEmail().toUpperCase()
                    .contains(email.trim().toUpperCase())));
        }
        if (phone != null) {
            predicate = predicate.and((account) -> (account.getPhone() != null && account.getPhone()
                    .contains(phone.trim())));
        }
        if (role != null) {
            predicate = predicate.and((account) -> (account.getRole().getName()
                    .equalsIgnoreCase(role.trim())));
        }
        if (id != null) {
            predicate = predicate.and((account) -> (account.getId().toString().toUpperCase()
                    .contains(id.trim().toUpperCase())));
        }
        return predicate;
    }
}
