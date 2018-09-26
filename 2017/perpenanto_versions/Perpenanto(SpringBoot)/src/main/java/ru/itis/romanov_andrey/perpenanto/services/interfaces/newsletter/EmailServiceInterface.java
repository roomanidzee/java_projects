package ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter;

/**
 * 17.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface EmailServiceInterface {
    void sendMail(String text, String subject, String email);
}
