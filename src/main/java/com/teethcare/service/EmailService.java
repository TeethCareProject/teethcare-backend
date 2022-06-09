package com.teethcare.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

public interface EmailService {
//    void sendSimpleMessage(String to, String subject, String text);
     void sendmail() throws AddressException, MessagingException, IOException;
}
