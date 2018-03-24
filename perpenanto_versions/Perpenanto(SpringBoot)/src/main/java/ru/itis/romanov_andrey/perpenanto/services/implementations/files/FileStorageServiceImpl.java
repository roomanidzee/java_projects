package ru.itis.romanov_andrey.perpenanto.services.implementations.files;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.repositories.FileInfoRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.FileStorageServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.PhotoServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.FileStorageUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 02.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class FileStorageServiceImpl implements FileStorageServiceInterface {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private PhotoServiceInterface photoService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Override
    public boolean saveFile(MultipartFile file, Profile profile) {

        FileInfo fileInfo = this.fileStorageUtil.convertFromMultipart(file);

        String type = fileInfo.getType();

        if(type.equals("image/jpeg")){

            Optional<FileInfo> photoImage = this.photoService.getProfilePhoto(profile);

            photoImage.ifPresent(fileInfo2 -> profile.getFiles()
                                                     .removeIf(fileInfo1 -> fileInfo1.equals(fileInfo2)));

            photoImage.ifPresent(fileInfo1 -> this.fileInfoRepository.delete(fileInfo1));

        }

        if(profile.getFiles() == null){

            Set<FileInfo> files = new HashSet<>();
            files.add(fileInfo);
            profile.setFiles(files);

        }else{
            profile.getFiles().add(fileInfo);
        }

        this.profileRepository.save(profile);

        this.fileStorageUtil.copyToStorage(file, fileInfo.getStorageFileName());
        return !(fileInfo.getStorageFileName() == null);

    }

    @Override
    @SneakyThrows
    public void writeFileToResponse(String storageFileName, HttpServletResponse response) {

        FileInfo file = this.fileInfoRepository.findByStorageFileName(storageFileName);

        response.setContentType(file.getType());
        InputStream is = new FileInputStream(new File(file.getUrl()));
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();

    }
}
