package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProfileForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProfileAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProfileAdminServiceImpl implements ProfileAdminService{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

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
