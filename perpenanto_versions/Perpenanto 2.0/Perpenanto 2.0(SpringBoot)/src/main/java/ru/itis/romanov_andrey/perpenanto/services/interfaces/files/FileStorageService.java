package ru.itis.romanov_andrey.perpenanto.services.interfaces.files;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;

import javax.servlet.http.HttpServletResponse;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface FileStorageService {

    boolean saveFile(MultipartFile file, Profile profile);
    void writeFileToResponse(String storageFileName, HttpServletResponse response);

}
