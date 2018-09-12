package io.vscale.perpenanto.controllers.auth;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.forms.user.UserRegistrationForm;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.services.interfaces.admin.AdminService;
import io.vscale.perpenanto.services.interfaces.auth.RegistrationService;
import io.vscale.perpenanto.services.interfaces.newsletter.SMSService;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;
import io.vscale.perpenanto.services.interfaces.user.UserService;
import io.vscale.perpenanto.validators.user.UserRegistrationFormValidator;

import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationController {

    private RegistrationService registrationService;
    private SMSService smsService;
    private AdminService adminService;
    private UserService userService;
    private ProfileService profileService;
    private UserRegistrationFormValidator userRegistrationFormValidator;

    @InitBinder("userForm")
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.userRegistrationFormValidator);
    }

    @GetMapping("/register")
    public ModelAndView getRegistrationPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/registration");
        modelAndView.addObject("userForm", new UserRegistrationForm());

        return modelAndView;
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("userForm") UserRegistrationForm userRegistrationForm,
                           BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("userForm", userRegistrationForm);
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
