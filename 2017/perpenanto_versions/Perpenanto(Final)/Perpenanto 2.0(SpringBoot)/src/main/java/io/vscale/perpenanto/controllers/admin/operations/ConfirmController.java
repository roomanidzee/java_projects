package io.vscale.perpenanto.controllers.admin.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.security.states.UserState;
import io.vscale.perpenanto.services.interfaces.user.UserService;

import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ConfirmController {

    private final UserService userService;

    @Autowired
    public ConfirmController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/confirm/{confirm-hash}")
    public ModelAndView confirmUser(@PathVariable("confirm-hash") String confirmHash){

        Optional<User> existedUser = this.userService.findByConfirmHash(confirmHash);

        if(!existedUser.isPresent()){
            throw new NullPointerException("User not found");
        }

        existedUser.ifPresent(user -> {
            user.setUserState(UserState.CONFIRMED);
            this.userService.updateUser(user);
        });

        return new ModelAndView("auth/success_registration");

    }

}
