package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import org.springframework.stereotype.Component;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.FileToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.FileToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 09.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class FileToUserTransferImpl implements FileToUserTransferInterface{

    @Override
    public List<FileToUser> getFilesToUsers(List<FileInfo> files) {

        List<FileToUser> resultList = new ArrayList<>();

        IntStream.range(0, files.size())
                 .forEachOrdered(i -> {

                        FileInfo file = files.get(i);
                        List<Profile> profiles = files.get(i).getProfiles();
                        profiles.stream()
                                .map(profile -> FileToUser.builder()
                                                          .fileId(file.getId())
                                                          .userId(profile.getUser().getId())
                                                          .build()
                                )
                                .forEachOrdered(resultList::add);

                 });

        return resultList;

    }
}
