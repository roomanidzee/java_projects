package ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter;

import java.util.concurrent.Future;

/**
 * 24.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface SMSServiceInterface {

    boolean sendSMS(String phone, String text);

}
