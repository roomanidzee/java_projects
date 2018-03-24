package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserServiceInterface;

/**
 * 26.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("User Confirm Controller")
public class ConfirmController {

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/confirm/{confirm-hash}")
    public ModelAndView confirmUser(@PathVariable("confirm-hash") String confirmHash){

        User user = this.userService.findByConfirmHash(confirmHash)
                                    .orElseThrow(() -> new NullPointerException("User not found"));

        user.setState(UserState.CONFIRMED);
        this.userService.saveOrUpdate(user);
        return new ModelAndView("auth/success_registration");

    }


}
