package com.romanidze.asyncappdbstorage.controllers;

import com.romanidze.asyncappdbstorage.dto.cat.CatImageDTO;
import com.romanidze.asyncappdbstorage.services.interfaces.cat.CatImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 04.10.2018
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

    @PostMapping(value = "/add_cat", produces = "application/json")
    public ResponseEntity<String> addCat(@RequestBody CatImageDTO catImageDTO){

        this.catImageService.saveImage(catImageDTO);

        return ResponseEntity.ok("{\"message\": \"Картинка сохранена\"");

    }

}
