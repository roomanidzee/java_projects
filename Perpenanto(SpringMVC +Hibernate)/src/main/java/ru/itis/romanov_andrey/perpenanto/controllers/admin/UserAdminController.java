package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.UserServiceInterface;

import java.util.List;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("UserAdmin Controller Annotation")
public class UserAdminController {

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/admin/security")
    public ModelAndView getSortedUsers(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        List<User> users = this.userService.getUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("admin/users_admin");

        return modelAndView;
    }

    @PostMapping(value = "/admin/security", params = "form_action")
    public ModelAndView workWithUsers(@RequestParam("form_action") String action,
                                      @ModelAttribute User user){

        if (action.equals("add") || action.equals("update")) {
            this.userService.saveOrUpdate(user);
        } else if (action.equals("delete")) {
            this.userService.delete(user.getId());
        }

        List<User> users = this.userService.getUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("admin/users_admin");

        return modelAndView;

    }

}
