package io.vscale.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.admin.ProfileForm;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.repositories.interfaces.ProfileRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.services.interfaces.admin.ProfileAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProfileAdminServiceImpl implements ProfileAdminService{

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    @Autowired
    public ProfileAdminServiceImpl(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addProfile(ProfileForm profileForm) {

        Profile profile = Profile.builder()
                                 .user(this.userRepository.find(profileForm.getUserId()))
                                 .name(profileForm.getPersonName())
                                 .surname(profileForm.getPersonSurname())
                                 .email(profileForm.getEmail())
                                 .build();
        this.profileRepository.save(profile);

    }

    @Override
    public void updateProfile(ProfileForm profileForm) {

        Profile profile = Profile.builder()
                                 .id(profileForm.getId())
                                 .user(this.userRepository.find(profileForm.getUserId()))
                                 .name(profileForm.getPersonName())
                                 .surname(profileForm.getPersonSurname())
                                 .email(profileForm.getEmail())
                                 .build();
        this.profileRepository.update(profile);

    }

    @Override
    public void deleteProfile(Long id) {
        this.profileRepository.delete(id);
    }
}
