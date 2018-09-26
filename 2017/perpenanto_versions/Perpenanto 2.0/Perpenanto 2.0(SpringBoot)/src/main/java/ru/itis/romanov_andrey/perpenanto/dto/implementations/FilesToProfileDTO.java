package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FilesToProfile;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.FilesToProfileTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * 01.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class FilesToProfileDTO implements EntityDTOInterface<FilesToProfileTransfer, FilesToProfile>{

    @Override
    public List<FilesToProfileTransfer> convert(List<FilesToProfile> originalList) {

        List<FilesToProfileTransfer> resultList = new ArrayList<>();

        originalList.forEach(filesToProfile ->
                  filesToProfile.getFiles()
                                .forEach(fileInfo ->
                                        resultList.add(FilesToProfileTransfer.builder()
                                                                             .profileId(filesToProfile.getProfile()
                                                                                                      .getId())
                                                                             .fileId(fileInfo.getId())
                                                                             .build())));

        return resultList;

    }
}
