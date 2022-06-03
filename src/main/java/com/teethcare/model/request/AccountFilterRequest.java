package com.teethcare.model.request;

import com.teethcare.model.entity.Account;
import com.teethcare.utils.StringUtils;
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
        Predicate<Account> predicate = account -> true;
        if (fullName != null) {
            predicate = predicate.and(account -> StringUtils.containsIgnoreCase((account.getLastName() + " " + account.getFirstName()), fullName));
        }
        if (username != null) {
            predicate = predicate.and(account -> StringUtils.containsIgnoreCase(account.getUsername(), username));
        }
        if (status != null) {
            predicate = predicate.and(account -> StringUtils.equalsIgnoreCase(account.getStatus(), status));
        }
        if (email != null) {
            predicate = predicate.and(account -> StringUtils.containsIgnoreCase(account.getEmail(), email));
        }
        if (phone != null) {
            predicate = predicate.and(account -> StringUtils.containsIgnoreCase(account.getPhone(), phone));
        }
        if (role != null) {
            predicate = predicate.and(account -> StringUtils.equalsIgnoreCase(account.getRole().getName(), role));
        }
        if (id != null) {
            predicate = predicate.and(account -> StringUtils.containsIgnoreCase(account.getId().toString(), id));
        }
        return predicate;
    }
}
