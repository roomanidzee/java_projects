package ru.itis.romanov_andrey.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.forms.admin.UserForm;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.UserAdminService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdminService userAdminService;

    @GetMapping("/security")
    public ModelAndView getUsers(@CookieValue(value = "status", defaultValue = "reset") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/users_admin");
        modelAndView.addObject("users", this.userService.getUsersByRoleAndCookie(Role.USER, cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/security", params = "add_action")
    public ModelAndView saveNewUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                    @ModelAttribute("userForm") UserForm userForm){

        this.userAdminService.addUser(userForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/users_admin");
        modelAndView.addObject("users", this.userService.getUsersByRoleAndCookie(Role.USER, cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/security", params = "update_action")
    public ModelAndView updateExistedUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                          @ModelAttribute("userForm") UserForm userForm){

        this.userAdminService.updateUser(userForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/users_admin");
        modelAndView.addObject("users", this.userService.getUsersByRoleAndCookie(Role.USER, cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/security", params = "delete_action")
    public ModelAndView deleteUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                   @RequestParam("id") Long id){

        this.userAdminService.deleteUser(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/users_admin");
        modelAndView.addObject("users", this.userService.getUsersByRoleAndCookie(Role.USER, cookieValue));

        return modelAndView;
    }

}
