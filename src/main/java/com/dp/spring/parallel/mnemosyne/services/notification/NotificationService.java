package com.dp.spring.parallel.mnemosyne.services.notification;

/**
 * Generic behaviour a generic notification service should have.
 */
public interface NotificationService {
    void notify(Object who, String title, String message);
}
