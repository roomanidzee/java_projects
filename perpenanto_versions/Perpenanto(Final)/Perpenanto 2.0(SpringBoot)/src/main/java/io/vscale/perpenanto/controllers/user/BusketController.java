package io.vscale.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.utils.userutils.BusketInformation;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.services.interfaces.auth.AuthenticationService;
import io.vscale.perpenanto.services.interfaces.user.BusketService;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;

import java.util.Map;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class BusketController {

   private final BusketService busketService;
   private final AuthenticationService authenticationService;
   private final ProfileService profileService;

    @Autowired
    public BusketController(BusketService busketService, AuthenticationService authenticationService,
                            ProfileService profileService) {
        this.busketService = busketService;
        this.authenticationService = authenticationService;
        this.profileService = profileService;
    }

    @GetMapping("/user/busket")
    public ModelAndView getProductsFromBusket(Authentication authentication){

       User user = this.authenticationService.getUserByAuthentication(authentication);
       Profile profile = this.profileService.findByUserId(user.getId());
       Map<Product, Long> products = this.busketService.getProductsFromBusket(profile);

       BusketInformation busketInformation = this.busketService.getBusketPrice(profile);

       ModelAndView modelAndView = new ModelAndView();
       modelAndView.addObject("products", products);
       modelAndView.addObject("reservationPrice", busketInformation.getPrice());
       modelAndView.addObject("timestamp", busketInformation.getReservation().getCreatedAt());
       modelAndView.setViewName("user/busket");

       return modelAndView;

    }

    @PostMapping("/add_product")
    public ModelAndView addProductToBusket(Authentication authentication,
                                           @RequestParam("productId") Long productId){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());
        this.busketService.addProductToBusket(profile, productId);

        Map<Product, Long> products = this.busketService.getProductsFromBusket(profile);

        BusketInformation busketInformation = this.busketService.getBusketPrice(profile);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("reservationPrice", busketInformation.getPrice());
        modelAndView.addObject("timestamp", busketInformation.getReservation().getCreatedAt());
        modelAndView.setViewName("user/busket");

        return modelAndView;

    }

    @PostMapping("/remove_product")
    public ModelAndView removeProductFromBusket(Authentication authentication,
                                                @RequestParam("productId") Long productId){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());
        this.busketService.removeProductFromBusket(profile, productId);

        Map<Product, Long> products = this.busketService.getProductsFromBusket(profile);

        BusketInformation busketInformation = this.busketService.getBusketPrice(profile);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("reservationPrice", busketInformation.getPrice());
        modelAndView.addObject("timestamp", busketInformation.getReservation().getCreatedAt());
        modelAndView.setViewName("user/busket");

        return modelAndView;

    }

}
