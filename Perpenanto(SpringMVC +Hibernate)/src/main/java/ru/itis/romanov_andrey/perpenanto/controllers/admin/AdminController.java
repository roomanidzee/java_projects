package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Admin Controller Annotation")
public class AdminController {

    @GetMapping("/admin/index")
    public ModelAndView getAdminIndexPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/admin_page");

        return modelAndView;

    }

}
