package ru.itis.romanov_andrey.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProfileForm;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProfileAdminService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class ProfileAdminController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileAdminService profileAdminService;

    @GetMapping("/profiles")
    public ModelAndView getProfiles(@CookieValue(value = "status", defaultValue = "reset") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/profiles_admin");
        modelAndView.addObject("profiles", this.profileService.getProfilesByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/profiles", params = "add_action")
    public ModelAndView saveNewProfile(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                       @ModelAttribute("profileForm") ProfileForm profileForm){

        this.profileAdminService.addProfile(profileForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/profiles_admin");
        modelAndView.addObject("profiles", this.profileService.getProfilesByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/profiles", params = "update_action")
    public ModelAndView updateProfile(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                      @ModelAttribute("profileForm") ProfileForm profileForm){

        this.profileAdminService.updateProfile(profileForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/profiles_admin");
        modelAndView.addObject("profiles", this.profileService.getProfilesByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/profiles", params = "delete_action")
    public ModelAndView updateProfile(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                      @RequestParam("id") Long id){

        this.profileAdminService.deleteProfile(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/profiles_admin");
        modelAndView.addObject("profiles", this.profileService.getProfilesByCookie(cookieValue));

        return modelAndView;

    }
}
