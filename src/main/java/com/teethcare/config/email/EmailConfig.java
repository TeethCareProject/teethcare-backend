//package com.teethcare.config.email;
//
//import com.teethcare.common.Constant;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Date;
//import java.util.Properties;
//
//@Configuration
//public class EmailConfig {
//
//    @Bean
//    public JavaMailSender getJavaMailSender() throws MessagingException {
////        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
////        mailSender.setHost("smtp.gmail.com");
////        mailSender.setPort(587);
////
////        mailSender.setUsername(Constant.EMAIL.SENDER_EMAIL);
////        mailSender.setPassword(Constant.EMAIL.SENDER_PASSWORD);
////
////        Properties props = mailSender.getJavaMailProperties();
////        props.put("mail.transport.protocol", "smtp");
////        props.put("mail.smtp.auth", "true");
////        props.put("mail.smtp.starttls.enable", "true");
////        props.put("mail.debug", "true");
//
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "127.0.0.1");
//        props.put("mail.smtp.port", "25");
//        props.put("mail.debug", "true");
//        Session session = Session.getDefaultInstance(props);
//        MimeMessage message = new MimeMessage(session);
////        message.setFrom(new InternetAddress("admin@test.com"));
//        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("a@b.com"));
//        message.setSubject("Notification");
//        message.setText("Successful!", "UTF-8"); // as "text/plain"
//        message.setSentDate(new Date());
//        Transport.send(message);
//        return mailSender;
//    }
//
//}