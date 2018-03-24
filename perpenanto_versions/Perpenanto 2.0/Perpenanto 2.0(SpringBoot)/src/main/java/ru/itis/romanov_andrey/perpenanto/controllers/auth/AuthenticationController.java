package ru.itis.romanov_andrey.perpenanto.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String login(@ModelAttribute("model") ModelMap model, Authentication authentication,
                        @RequestParam Optional<String> error) {

        if (authentication != null) {
            return "redirect:/";
        }

        model.addAttribute("error", error);

        return "auth/login";

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Authentication authentication) {

        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/login";

    }

    @GetMapping("/")
    public String root(Authentication authentication) {

        if (authentication != null) {

            User user = this.authenticationService.getUserByAuthentication(authentication);

            if (user.getRole().equals(Role.USER)) {
                return "redirect:/user/profile";
            }else if(user.getRole().equals(Role.ADMIN)){
                return "redirect:/admin/index";
            }
        }

        return "redirect:/login";
    }

}
