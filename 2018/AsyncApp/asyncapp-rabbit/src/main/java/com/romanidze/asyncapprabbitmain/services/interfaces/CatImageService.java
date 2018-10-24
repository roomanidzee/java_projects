package com.romanidze.asyncapprabbitmain.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.romanidze.asyncapprabbitmain.dto.CatImageDTO;

/**
 * 07.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface CatImageService {

    void getImageOfCat(String username) throws JsonProcessingException;
    String addCatImage(CatImageDTO catImageDTO);

}
