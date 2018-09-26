package ru.itis.romanov_andrey.perpenanto.services.implementations.files;

import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.PhotoServiceInterface;

import java.util.Optional;
import java.util.Set;

/**
 * 02.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class PhotoServiceImpl implements PhotoServiceInterface{

    @Override
    public Optional<FileInfo> getProfilePhoto(Profile profile) {

        Set<FileInfo> profileFiles = profile.getFiles();

        return profileFiles.stream()
                           .filter(fileInfo -> fileInfo.getType().equals("image/jpeg"))
                           .findFirst();

    }
}
