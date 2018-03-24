package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserServiceInterface;

/**
 * 24.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Admin Operations Controller")
@RequestMapping("/admin/action")
public class OperationsController {

    @Autowired
    private AdminServiceInterface adminService;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/create_temp_password/{user-id}")
    public ModelAndView getNewPasswordOfUserPage(@PathVariable("user-id") Long userId){

        //необходимо для поиска, есть такой пользователь, или нет
        //если нет - пробрасываем ошибку, есть - работаем дальше, к User не присваиваем
        this.userService.findById(userId)
                        .orElseThrow(() -> new NullPointerException("User not found"));

        this.adminService.createTempPassword(userId);
        return new ModelAndView("email/temp_password_page");

    }


}
