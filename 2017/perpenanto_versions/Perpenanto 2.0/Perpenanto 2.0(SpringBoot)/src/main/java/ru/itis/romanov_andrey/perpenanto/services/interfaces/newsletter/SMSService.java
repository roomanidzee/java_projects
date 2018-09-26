package ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface SMSService {

    boolean sendSMS(String phone, String text);

}
