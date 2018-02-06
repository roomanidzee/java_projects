package ru.itis.romanov_andrey.perpenanto.services.interfaces.files;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;

import java.util.Optional;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface PhotoService {

    Optional<FileInfo> getProfilePhoto(Profile profile);

}
