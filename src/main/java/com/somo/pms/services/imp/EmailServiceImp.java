package com.somo.pms.services.imp;

import com.somo.pms.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;
    @Retryable(
            retryFor = MailSendException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public void sendEmailWithToken(String userEmail, String link, String projectName, LocalDateTime expired) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "You're invited to join the project team: " + projectName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        String formattedExpiry = expired.format(formatter);
        String htmlTemplate = """
            <!DOCTYPE html>
                             <html lang="en">
                             <head>
                               <meta charset="UTF-8">
                               <title>Project Team Invitation</title>
                               <style>
                                 body {
                                   font-family: Arial, sans-serif;
                                   background-color: #f4f6f8;
                                   margin: 0;
                                   padding: 0;
                                 }
                                 .email-container {
                                   max-width: 600px;
                                   margin: 30px auto;
                                   background-color: #ffffff;
                                   padding: 30px;
                                   border-radius: 8px;
                                   box-shadow: 0 4px 8px rgba(0,0,0,0.05);
                                 }
                                 h2 {
                                   color: #2c3e50;
                                 }
                                 p {
                                   color: #555555;
                                   line-height: 1.6;
                                 }
                                 .cta-button {
                                   display: inline-block;
                                   padding: 12px 24px;
                                   margin-top: 20px;
                                   background-color: #007bff;
                                   color: #ffffff !important;
                                   text-decoration: none;
                                   border-radius: 5px;
                                   font-weight: bold;
                                 }
                                 .footer {
                                   margin-top: 30px;
                                   font-size: 12px;
                                   color: #888888;
                                   text-align: center;
                                 }
                               </style>
                             </head>
                             <body>
                               <div class="email-container">
                                 <h2>Hello,</h2>
                                 <p>
                                   You’ve been invited to join the team for the project <strong>%s</strong>.
                                   We believe your skills and experience would be a great asset to our team.
                                 </p>
                        \s
                                 <p>
                                   Click the button below to accept the invitation and join us:
                                 </p>
                        \s
                                 <a href='%s' class="cta-button">Join the Team</a>
                        \s
                                 <p>
                                   <strong>Note:</strong> This invitation will expire on <strong>%s</strong>.
                                 </p>
                        \s
                                 <p>
                                   If you have any questions or need more details, feel free to reply to this email.
                                 </p>
                        \s
                                 <p>Looking forward to working together!</p>
                        \s
                                 <p>Best regards,<br>
                                 Project Team</p>
                        \s
                                 <div class="footer">
                                   If you weren't expecting this, you can safely ignore it.
                                 </div>
                               </div>
                             </body>
                             </html>
           """;
        String htmlContent = String.format(htmlTemplate, projectName, link,formattedExpiry);
        mimeMessageHelper.setTo(userEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent, true);
        try {
            mailSender.send(mimeMessage);
        }catch (MailSendException e){
            log.error(e.fillInStackTrace());
            throw new MailSendException("Fail to send mail");
        }
    }
}
