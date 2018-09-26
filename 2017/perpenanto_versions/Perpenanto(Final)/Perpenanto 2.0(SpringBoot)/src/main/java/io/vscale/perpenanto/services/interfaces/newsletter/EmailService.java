package io.vscale.perpenanto.services.interfaces.newsletter;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface EmailService {
    void sendMail(String text, String subject, String email);
}
