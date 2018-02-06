package ru.itis.romanov_andrey.perpenanto.services.implementations.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FilesToProfile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FilesToProfileRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.PhotoService;

import java.util.Collections;
import java.util.Set;
import java.util.Optional;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private FilesToProfileRepository filesToProfileRepository;

    @Override
    public Optional<FileInfo> getProfilePhoto(Profile profile) {

       Optional<FilesToProfile> existedFilesToProfile;

       try{

           FilesToProfile filesToProfile = DataAccessUtils.objectResult(
                   Collections.singletonList(this.filesToProfileRepository.find(profile.getId())), FilesToProfile.class
           );
           existedFilesToProfile = Optional.of(filesToProfile);

       }catch (IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){
           return Optional.empty();
       }

        Set<FileInfo> files =  existedFilesToProfile.get().getFiles();

        return files.stream()
                    .filter(file -> file.getType().equals("image/jpeg"))
                    .findFirst();

    }
}
