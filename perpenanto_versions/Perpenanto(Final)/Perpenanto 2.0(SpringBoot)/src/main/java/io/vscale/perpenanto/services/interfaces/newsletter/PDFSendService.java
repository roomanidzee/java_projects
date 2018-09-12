package io.vscale.perpenanto.services.interfaces.newsletter;

import io.vscale.perpenanto.models.usermodels.FileInfo;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;

import java.sql.Timestamp;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface PDFSendService {

    void sendPDF(FileInfo fileInfo, Profile profile);
    void sendEmailWithPDF(User user, Timestamp timestamp);

}
