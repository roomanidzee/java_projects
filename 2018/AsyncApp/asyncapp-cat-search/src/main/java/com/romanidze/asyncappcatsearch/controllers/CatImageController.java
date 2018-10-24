package com.romanidze.asyncappcatsearch.controllers;

import com.romanidze.asyncappcatsearch.dto.CatImageDTO;
import com.romanidze.asyncappcatsearch.services.interfaces.CatImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 06.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
public class CatImageController {

    private final CatImageService catImageService;

    @Autowired
    public CatImageController(CatImageService catImageService) {
        this.catImageService = catImageService;
    }

    @GetMapping("/cats/search")
    public ResponseEntity<CatImageDTO> getImageOfCat(){
        return ResponseEntity.ok(this.catImageService.getImageOfCat());
    }
}
