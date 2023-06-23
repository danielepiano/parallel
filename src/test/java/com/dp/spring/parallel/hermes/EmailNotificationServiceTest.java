package com.dp.spring.parallel.hermes;

import com.dp.spring.parallel.hermes.exceptions.FailureSendingEmail;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Spy
    JavaMailSender javaMailSender = new JavaMailSenderImpl();

    @InjectMocks
    @Spy
    EmailNotificationService emailNotificationService;


    @BeforeEach
    public void setUp() {
    }

    @Test
    void notify_whenSendWorks_shouldWork() {
        String who = "who-test";
        String title = "title-test";
        String message = "message-test";

        doNothing()
                .when(javaMailSender)
                .send(any(MimeMessage.class));

        emailNotificationService.notify(who, title, message);

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void notify_whenSendFails_shouldThrow() {
        String who = "who-test";
        String title = "title-test";
        String message = "message-test";

        doThrow(MailSendException.class)
                .when(javaMailSender)
                .send(any(MimeMessage.class));

        assertThrows(FailureSendingEmail.class, () -> emailNotificationService.notify(who, title, message));

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void buildMessage_when_should() {
        String path = "first-name-template.html";
        String param = "name-test";
        Map<EmailMessageParser.Placeholder, String> params = Map.of(EmailMessageParser.Placeholder.FIRST_NAME, param);

        String parsed = emailNotificationService.buildMessage(path, params);
        assertEquals(param, parsed, "message not parsed properly");
    }


}
