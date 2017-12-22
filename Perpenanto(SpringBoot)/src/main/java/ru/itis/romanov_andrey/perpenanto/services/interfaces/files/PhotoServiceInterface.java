package ru.itis.romanov_andrey.perpenanto.services.interfaces.files;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;

import java.util.Optional;

/**
 * 02.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface PhotoServiceInterface {

    Optional<FileInfo> getProfilePhoto(Profile profile);

}
