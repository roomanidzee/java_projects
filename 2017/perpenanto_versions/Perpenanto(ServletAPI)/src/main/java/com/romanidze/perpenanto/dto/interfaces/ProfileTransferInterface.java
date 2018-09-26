package com.romanidze.perpenanto.dto.interfaces;

import com.romanidze.perpenanto.models.Profile;
import com.romanidze.perpenanto.models.temp.TempProfile;

import java.util.List;

public interface ProfileTransferInterface {

    List<TempProfile> getTempProfiles(List<Profile> oldList);

}
