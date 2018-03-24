package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ProfileForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileAdminService {

    void addProfile(ProfileForm profileForm);
    void updateProfile(ProfileForm profileForm);
    void deleteProfile(Long id);

}
