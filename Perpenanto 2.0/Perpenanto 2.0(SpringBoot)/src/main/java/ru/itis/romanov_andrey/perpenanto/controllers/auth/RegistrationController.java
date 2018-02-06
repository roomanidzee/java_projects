package ru.itis.romanov_andrey.perpenanto.controllers.auth;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AdminService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.RegistrationService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.SMSService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserService;
import ru.itis.romanov_andrey.perpenanto.validators.UserRegistrationFormValidator;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRegistrationFormValidator userRegistrationFormValidator;

    @InitBinder
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.userRegistrationFormValidator);
    }

    @GetMapping("/register")
    public ModelAndView getRegistrationPage(){
        return new ModelAndView("auth/registration");
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userForm") UserRegistrationForm userRegistrationForm,
                                                              BindingResult errors, RedirectAttributes attributes){

        if(errors.hasErrors()){
            attributes.addFlashAttribute("error", errors.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/register";
        }

        this.registrationService.register(userRegistrationForm);

        Optional<User> newUser = this.userService.findByLogin(userRegistrationForm.getLogin());

        newUser.ifPresent(user ->{

            this.adminService.generateConfirmLink(user);

            Profile profile = this.profileService.findByUserId(user.getId());
            StringBuilder sb = new StringBuilder();
            sb.append("Здравствуйте, ").append(profile.getSurname()).append(" ").append(profile.getName())
                    .append("! Удачных вам покупок!");

            boolean sendResult = this.smsService.sendSMS(profile.getPhone(), sb.toString());

            if(!sendResult){
                throw new NullPointerException("Ничего не отправлено!");
            }

        });

        return "auth/confirm_registration";

    }

}
