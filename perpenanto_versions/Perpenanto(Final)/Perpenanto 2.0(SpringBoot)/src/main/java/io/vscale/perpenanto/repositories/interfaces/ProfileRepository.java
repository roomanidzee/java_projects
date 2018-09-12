package io.vscale.perpenanto.repositories.interfaces;

import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileRepository extends CrudDAOInterface<Profile, Long> {

    Profile findByUser(User user);

}
