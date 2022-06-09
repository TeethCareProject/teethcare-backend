package com.teethcare.service;

import javax.mail.MessagingException;

public interface EmailService {
    void sendStaffCreatingPasswordEmail(String to, String fwdLink) throws MessagingException;
}
