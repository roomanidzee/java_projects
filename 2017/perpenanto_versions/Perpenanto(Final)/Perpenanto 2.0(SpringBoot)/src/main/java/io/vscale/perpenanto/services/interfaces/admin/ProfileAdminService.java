package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.forms.admin.ProfileForm;

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
