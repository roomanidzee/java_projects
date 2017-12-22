package ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * 09.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface SendPDFServiceInterface {

    void sendPDF(FileInfo fileInfo, Profile profile);
    void sendEmailWithPDF(User user, Timestamp timestamp);

}
