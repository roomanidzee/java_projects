package com.romanidze.asyncappdbstorage.services.implementations.cat;

import com.romanidze.asyncappdbstorage.domain.CatImage;
import com.romanidze.asyncappdbstorage.dto.cat.CatImageDTO;
import com.romanidze.asyncappdbstorage.repositories.CatImageRepository;
import com.romanidze.asyncappdbstorage.services.interfaces.cat.CatImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

@Service
public class CatImageServiceImpl implements CatImageService {

    private final CatImageRepository catImageRepository;

    @Autowired
    public CatImageServiceImpl(CatImageRepository catImageRepository) {
        this.catImageRepository = catImageRepository;
    }

    @Override
    public void saveImage(CatImageDTO catImageDTO) {

        CatImage catImage = CatImage.builder()
                                    .idOfImageFromAPI(catImageDTO.getIdOfImageFromAPI())
                                    .imageURL(catImageDTO.getImageURL())
                                    .build();

        this.catImageRepository.save(catImage);

    }
}
