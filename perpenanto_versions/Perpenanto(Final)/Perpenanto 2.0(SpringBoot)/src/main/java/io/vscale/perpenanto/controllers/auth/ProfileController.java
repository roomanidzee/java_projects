package io.vscale.perpenanto.controllers.auth;

import io.vscale.perpenanto.models.usermodels.FileInfo;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.services.interfaces.auth.AuthenticationService;
import io.vscale.perpenanto.services.interfaces.files.PhotoService;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ProfileController {

    private final AuthenticationService authenticationService;
    private final PhotoService photoService;
    private final ProfileService profileService;

    @Autowired
    public ProfileController(AuthenticationService authenticationService, PhotoService photoService,
                             ProfileService profileService) {
        this.authenticationService = authenticationService;
        this.photoService = photoService;
        this.profileService = profileService;
    }

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
