package ru.itis.romanov_andrey.perpenanto.services.implementations.files;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FilesToProfile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FileInfoRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FilesToProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.FileStorageService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.PhotoService;
import ru.itis.romanov_andrey.perpenanto.utils.FileStorageUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FilesToProfileRepository filesToProfileRepository;

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Override
    public boolean saveFile(MultipartFile file, Profile profile) {

        FileInfo fileInfo = this.fileStorageUtil.convertFromMultipart(file);
        String type = fileInfo.getType();

        if(type.equals("image/jpeg")){

            Optional<FileInfo> photoImage = this.photoService.getProfilePhoto(profile);

            photoImage.ifPresent(photo ->{

                this.filesToProfileRepository.delete(profile.getId(), photo.getId());
                this.fileInfoRepository.delete(photo.getId());

            });

        }

        this.fileInfoRepository.save(fileInfo);
        FilesToProfile filesToProfile = this.filesToProfileRepository.find(profile.getId());

        if(filesToProfile.getFiles() == null){
            filesToProfile.setFiles(Sets.newHashSet(fileInfo));
            this.filesToProfileRepository.update(filesToProfile);
        }else{
            filesToProfile.getFiles().add(fileInfo);
            this.filesToProfileRepository.update(filesToProfile);
        }

        this.fileStorageUtil.copyToStorage(file, fileInfo.getEncodedName());
        return !(fileInfo.getEncodedName() == null);

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
