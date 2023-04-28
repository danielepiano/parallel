package com.dp.spring.parallel.mnemosyne.services.email;

public interface EmailSender {
    void send(String to, String subject, String email);
}
