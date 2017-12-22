package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ProfileForm;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileAdminServiceInterface {

    void addProfile(ProfileForm profileForm);
    void updateProfile(ProfileForm profileForm);
    void deleteProfile(Long id);

}
