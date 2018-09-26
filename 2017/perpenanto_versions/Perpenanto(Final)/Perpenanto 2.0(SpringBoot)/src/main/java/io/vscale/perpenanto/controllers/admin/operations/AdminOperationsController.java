package io.vscale.perpenanto.controllers.admin.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.security.states.UserState;
import io.vscale.perpenanto.services.interfaces.admin.AdminService;
import io.vscale.perpenanto.services.interfaces.user.UserService;

import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/action")
public class AdminOperationsController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminOperationsController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/create_temp_password/{user-id}")
    public ModelAndView getNewPasswordOfUserPage(@PathVariable("user-id") Long userId){

        Optional<User> existedUser = this.userService.findById(userId);

        if(!existedUser.isPresent()){
            throw new NullPointerException("User not found");
        }

        existedUser.ifPresent(user -> this.adminService.createTempPassword(userId));
        return new ModelAndView("redirect:/admin/statistics/security");
    }

    @GetMapping("/block_user/{user-id}")
    public ModelAndView blockUser(@PathVariable("user-id") Long userId){

        Optional<User> existedUser = this.userService.findById(userId);

        if(!existedUser.isPresent()){
            throw new NullPointerException("User not found");
        }

        existedUser.ifPresent(user -> {
            user.setUserState(UserState.BANNED);
            this.userService.updateUser(user);
        });

        return new ModelAndView("redirect:/admin/statistics/security");

    }

    @GetMapping("/unblock_user/{user-id}")
    public ModelAndView unblockUser(@PathVariable("user-id") Long userId){

        Optional<User> existedUser = this.userService.findById(userId);

        if(!existedUser.isPresent()){
            throw new NullPointerException("User not found");
        }

        existedUser.ifPresent(user -> {
            user.setUserState(UserState.CONFIRMED);
            this.userService.updateUser(user);
        });

        return new ModelAndView("redirect:/admin/statistics/security");

    }

}
