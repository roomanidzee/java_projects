package io.vscale.perpenanto.services.implementations.files;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.vscale.perpenanto.models.usermodels.FileInfo;
import io.vscale.perpenanto.models.usermodels.FilesToProfile;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.repositories.interfaces.FileInfoRepository;
import io.vscale.perpenanto.repositories.interfaces.FilesToProfileRepository;
import io.vscale.perpenanto.services.interfaces.files.FileStorageService;
import io.vscale.perpenanto.services.interfaces.files.PhotoService;
import io.vscale.perpenanto.utils.dbutils.FileStorageUtil;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

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
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FileStorageServiceImpl implements FileStorageService {

    private FileInfoRepository fileInfoRepository;
    private PhotoService photoService;
    private FilesToProfileRepository filesToProfileRepository;
    private FileStorageUtil fileStorageUtil;
    private JdbcTemplateWrapper<FilesToProfile> jdbcTemplateWrapper;

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

        Optional<FilesToProfile> tempFilesToProfile =
                this.jdbcTemplateWrapper.findItem(this.filesToProfileRepository, profile.getId());

        tempFilesToProfile.ifPresent(filesToProfile1 -> {

            if(filesToProfile1.getFiles() == null){
                filesToProfile1.setFiles(Sets.newHashSet(fileInfo));
                this.filesToProfileRepository.update(filesToProfile1);
            }else{
                filesToProfile1.getFiles().add(fileInfo);
                this.filesToProfileRepository.update(filesToProfile1);
            }

        });

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
