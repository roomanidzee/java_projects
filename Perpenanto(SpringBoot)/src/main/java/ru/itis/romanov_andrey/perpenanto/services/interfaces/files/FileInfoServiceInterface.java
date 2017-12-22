package ru.itis.romanov_andrey.perpenanto.services.interfaces.files;

import ru.itis.romanov_andrey.perpenanto.models.adminmodels.FileToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;

import java.util.List;

/**
 * 09.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface FileInfoServiceInterface {

    List<FileInfo> getFiles();
    List<FileToUser> getFileToUser(List<FileInfo> files);

}
