package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProfileForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProfileAdminServiceInterface;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProfileAdminServiceImpl implements ProfileAdminServiceInterface{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addProfile(ProfileForm profileForm) {

        User user = this.userRepository.findOne(profileForm.getUserId());

        Profile profile = Profile.builder()
                                 .personName(profileForm.getPersonName())
                                 .personSurname(profileForm.getPersonSurname())
                                 .email(profileForm.getEmail())
                                 .user(user)
                                 .build();

        this.profileRepository.save(profile);

    }

    @Override
    public void updateProfile(ProfileForm profileForm) {

        Profile profile = this.profileRepository.findOne(profileForm.getId());
        User user = this.userRepository.findOne(profileForm.getUserId());

        if(profile == null){
            throw new NullPointerException("Profile not found!");
        }

        profile.setPersonName(profileForm.getPersonName());
        profile.setPersonSurname(profileForm.getPersonSurname());
        profile.setEmail(profileForm.getEmail());
        profile.setUser(user);

        this.profileRepository.save(profile);

    }

    @Override
    public void deleteProfile(Long id) {
        this.profileRepository.delete(id);
    }
}
