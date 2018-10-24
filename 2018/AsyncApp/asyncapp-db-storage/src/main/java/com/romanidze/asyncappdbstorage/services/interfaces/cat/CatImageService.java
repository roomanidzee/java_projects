package com.romanidze.asyncappdbstorage.services.interfaces.cat;

import com.romanidze.asyncappdbstorage.dto.cat.CatImageDTO;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface CatImageService {

    void saveImage(CatImageDTO catImageDTO);

}
