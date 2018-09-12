package io.vscale.perpenanto.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.vscale.perpenanto.services.interfaces.files.FileStorageService;

import javax.servlet.http.HttpServletResponse;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ProfileUtilsController {

    private final FileStorageService fileStorageService;

    @Autowired
    public ProfileUtilsController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/get_photo/{file-name:.+}")
    public void getPhoto(@PathVariable("file-name") String fileName, HttpServletResponse response){
        this.fileStorageService.writeFileToResponse(fileName, response);
    }

}
