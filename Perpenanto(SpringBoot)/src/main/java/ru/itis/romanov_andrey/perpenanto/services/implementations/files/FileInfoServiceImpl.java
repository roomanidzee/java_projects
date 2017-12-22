package ru.itis.romanov_andrey.perpenanto.services.implementations.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.FileToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.FileToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.repositories.FileInfoRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.FileInfoServiceInterface;

import java.util.List;

/**
 * 09.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class FileInfoServiceImpl implements FileInfoServiceInterface{

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FileToUserTransferInterface fileInfoDTO;

    @Override
    public List<FileInfo> getFiles() {
        return this.fileInfoRepository.findAll();
    }

    @Override
    public List<FileToUser> getFileToUser(List<FileInfo> files) {
        return this.fileInfoDTO.getFilesToUsers(files);
    }
}
