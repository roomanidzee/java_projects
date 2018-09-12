package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.models.usermodels.User;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AdminService {

    void createTempPassword(Long userId);
    void generateConfirmLink(User user);

}
