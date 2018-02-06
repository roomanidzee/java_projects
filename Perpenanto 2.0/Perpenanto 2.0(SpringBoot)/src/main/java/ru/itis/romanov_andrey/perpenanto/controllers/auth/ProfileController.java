package ru.itis.romanov_andrey.perpenanto.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.PhotoService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;

import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ProfileController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/user/profile")
    public ModelAndView getProfilePage(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());
        ModelAndView modelAndView = new ModelAndView();

        Optional<FileInfo> profilePhoto = this.photoService.getProfilePhoto(profile);
        profilePhoto.ifPresent(file -> modelAndView.addObject("photo", file.getEncodedName()));

        modelAndView.addObject("reservations", this.profileService.getReservationInformation(user));
        modelAndView.addObject("products", this.profileService.getProductsByUser(user));
        modelAndView.addObject("reservationCount", this.profileService.countReservations(user));
        modelAndView.addObject("commonProductsPrice", this.profileService.getCommonProductsPrice(user));
        modelAndView.addObject("spendedMoneyOnReservations",
                                                               this.profileService.getSpendedMoneyOnReservations(user));
        modelAndView.addObject("soldedProductsCount", this.profileService.getSoldedProductsCount(user));
        modelAndView.addObject("profile", profile);
        modelAndView.addObject("addresses", this.profileService.getAddressesByUser(user));

        modelAndView.setViewName("auth/profile");
        return modelAndView;

    }

}
