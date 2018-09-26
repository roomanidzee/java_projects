package io.vscale.perpenanto.services.implementations.files;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.models.usermodels.FileInfo;
import io.vscale.perpenanto.models.usermodels.FilesToProfile;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.repositories.interfaces.FilesToProfileRepository;
import io.vscale.perpenanto.services.interfaces.files.PhotoService;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

import java.util.Optional;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PhotoServiceImpl implements PhotoService {

    private final FilesToProfileRepository filesToProfileRepository;
    private JdbcTemplateWrapper<FilesToProfile> jdbcTemplateWrapper;

    @Override
    public Optional<FileInfo> getProfilePhoto(Profile profile) {

        Optional<FilesToProfile> tempFilesToProfile =
                this.jdbcTemplateWrapper.findItem(this.filesToProfileRepository, profile.getId());

        if (!tempFilesToProfile.isPresent()) {
            return Optional.empty();
        }

        FilesToProfile existedFilesToProfile = tempFilesToProfile.get();

        return existedFilesToProfile.getFiles().stream()
                                               .filter(file -> file.getType().equals("image/jpeg"))
                                               .findFirst();

    }
}
