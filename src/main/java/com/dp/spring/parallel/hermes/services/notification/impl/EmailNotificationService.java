package com.dp.spring.parallel.hermes.services.notification.impl;

import com.dp.spring.parallel.common.utils.ResourcesUtils;
import com.dp.spring.parallel.hermes.config.EmailConfig;
import com.dp.spring.parallel.hermes.exceptions.FailureSendingEmail;
import com.dp.spring.parallel.hermes.services.notification.NotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link NotificationService} implementation for email notification.<br>
 * <b>Adapting</b> legacy {@link JavaMailSender} interface to be used as a {@link NotificationService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
            log.info("Sending email to {}: {}", who.toString(), title);

            final MimeMessage legacyMessage = this.legacyMailSender.createMimeMessage();
            final MimeMessageHelper legacyMessageHelper = new MimeMessageHelper(legacyMessage, EmailConfig.DEFAULT_ENCODING);
            legacyMessageHelper.setFrom(EmailConfig.FROM);
            legacyMessageHelper.setTo((String) who);
            legacyMessageHelper.setSubject(title);
            legacyMessageHelper.setText(message, true);

            this.legacyMailSender.send(legacyMessage);

            log.info("Email sent to {}: {}", who, title);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailureSendingEmail(e);
        }
    }

    /**
     * Building an email notification message out of a template file in resources.
     *
     * @param templatePath  the path for the template
     * @param parsingParams parameters to be parsed on the raw message
     * @return the parsed and ready to send message
     */
    public String buildMessage(
            final String templatePath,
            final Map<EmailMessageParser.Placeholder, String> parsingParams
    ) {
        // Reading email message and http template from file
        final String rawMessage = ResourcesUtils.readFileAsString(templatePath);

        // Parsing the message, replacing keywords in curly brackets with proper values
        return EmailMessageParser.parse(rawMessage, parsingParams);
    }
}
