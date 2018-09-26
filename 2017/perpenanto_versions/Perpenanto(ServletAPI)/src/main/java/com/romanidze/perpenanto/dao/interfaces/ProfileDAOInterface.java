package com.romanidze.perpenanto.dao.interfaces;

import com.romanidze.perpenanto.models.Profile;
import com.romanidze.perpenanto.models.User;

public interface ProfileDAOInterface extends CrudDAOInterface<Profile, Long>{

    Profile findByUser(User user);

}
