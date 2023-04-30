package com.dp.spring.parallel.mnemosyne.services.notification;

import com.dp.spring.parallel.mnemosyne.config.EmailConfig;
import com.dp.spring.parallel.mnemosyne.exceptions.FailureSendingEmail;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * {@link NotificationService} implementation for email notification.<br>
 * <b>Adapting</b> legacy {@link JavaMailSender} interface to be used as a {@link NotificationService}.
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender legacyMailSender;


    /**
     * Sending email notification, adapting {@link NotificationService} params to {@link MimeMessage} param of
     * of the legacy {@link JavaMailSender}.<br>
     * The email is sent asynchronously so as not to further delay the request processing.
     *
     * @param who     the recipient of the email
     * @param title   the subject of the email
     * @param message the message of the email
     */
    @Override
    @Async
    public void notify(Object who, String title, String message) {
        try {
            final MimeMessage legacyMessage = this.legacyMailSender.createMimeMessage();
            final MimeMessageHelper legacyMessageHelper = new MimeMessageHelper(legacyMessage, EmailConfig.DEFAULT_ENCODING);
            legacyMessageHelper.setFrom(EmailConfig.FROM);
            legacyMessageHelper.setTo((String) who);
            legacyMessageHelper.setSubject(title);
            legacyMessageHelper.setText(message, true);

            this.legacyMailSender.send(legacyMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailureSendingEmail(e);
        }
    }
}
