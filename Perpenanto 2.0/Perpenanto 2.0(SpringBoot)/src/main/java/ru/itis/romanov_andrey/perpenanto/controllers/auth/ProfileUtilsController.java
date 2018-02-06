package ru.itis.romanov_andrey.perpenanto.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.FileStorageService;

import javax.servlet.http.HttpServletResponse;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ProfileUtilsController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/get_photo/{file-name:.+}")
    public void getPhoto(@PathVariable("file-name") String fileName, HttpServletResponse response){
        this.fileStorageService.writeFileToResponse(fileName, response);
    }

}
