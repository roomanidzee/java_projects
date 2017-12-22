package ru.itis.romanov_andrey.perpenanto.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.RegistrationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.SMSServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserServiceInterface;
import ru.itis.romanov_andrey.perpenanto.validators.UserRegistrationFormValidator;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Registration Controller Annotation")
public class RegistrationController {

    @Autowired
    private RegistrationServiceInterface registrationService;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private ProfileServiceInterface profileService;

    @Autowired
    private SMSServiceInterface smsService;

    @Autowired
    private AdminServiceInterface adminService;

    @Autowired
    private UserRegistrationFormValidator userRegistrationFormValidator;

    @InitBinder
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.userRegistrationFormValidator);
    }

    @PostMapping(value = "/register")
    public String register(@Valid @ModelAttribute("userForm") UserRegistrationForm userRegistrationForm,
                           BindingResult errors, RedirectAttributes attributes){

        if(errors.hasErrors()){

            attributes.addFlashAttribute("error", errors.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/register";

        }

        this.registrationService.register(userRegistrationForm);

        Optional<User> newUser = this.userService.findByLogin(userRegistrationForm.getLogin());

        newUser.ifPresent(user -> {

            this.adminService.generateHash(user);

            Profile profile = this.profileService.findByUserId(user.getId());
            StringBuilder sb = new StringBuilder();
            sb.append("Здравствуйте, ").append(profile.getPersonSurname()).append(" ").append(profile.getPersonName())
              .append("! Удачных вам покупок!");

            if(!(this.smsService.sendSMS(profile.getPhone(),sb.toString()))){
                throw new NullPointerException("Ничего не отправлено!");
            }

        });

        return "auth/confirm_registration";

    }

    @GetMapping(value = "/register")
    public ModelAndView getRegisterPage(){
        return new ModelAndView("auth/registration");
    }

}
