package com.dp.spring.parallel.mnemosyne.services.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService /*implements EmailSender*/ {
    /*private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String subject, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            messageHelper.setFrom(EmailConfig.FROM);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(email, true);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new FailureSendingEmail();
        }
    }*/
}
