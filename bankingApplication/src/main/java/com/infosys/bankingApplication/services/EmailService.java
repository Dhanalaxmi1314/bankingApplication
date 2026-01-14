package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.configs.MailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailConfig mailConfig;

    public EmailService(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public void sendLowBalanceAlert(String toEmail, String name, double balance) {

        Session session = Session.getInstance(
                mailConfig.getProperties(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                mailConfig.getUsername(),
                                mailConfig.getPassword()
                        );
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getUsername()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("⚠ Low Balance Alert");
            message.setText(
                    "Hello " + name + ",\n\n" +
                            "Your account balance is LOW.\n\n" +
                            "Current Balance: ₹" + balance + "\n\n" +
                            "Please deposit funds.\n\n" +
                            "— Banking System"
            );

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
