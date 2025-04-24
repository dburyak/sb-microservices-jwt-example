package com.dburyak.example.jwt.email.service;

import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailService {

    public void sendEmail(SendEmailMsg sendEmailReq) {
        // in a real world app we'd
        //  - retrieved the email template from the database
        //  - use a template engine to fill the template with the data from "params"
        //  - send the email using an email service provider (e.g. AWS SES, SendGrid, etc.)
        log.info("sending email: {}", sendEmailReq);
    }
}
