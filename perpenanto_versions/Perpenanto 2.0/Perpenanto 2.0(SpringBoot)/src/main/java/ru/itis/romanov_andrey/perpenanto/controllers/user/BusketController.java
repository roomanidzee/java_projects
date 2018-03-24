package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.forms.user.BusketInformation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;

import java.util.List;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class BusketController {

   @Autowired
   private BusketService busketService;

   @Autowired
   private AuthenticationService authenticationService;

   @Autowired
   private ProfileService profileService;

   @GetMapping("/user/busket")
   public ModelAndView getProductsFromBusket(Authentication authentication){

       User user = this.authenticationService.getUserByAuthentication(authentication);
       Profile profile = this.profileService.findByUserId(user.getId());
       List<Product> products = this.busketService.getProductsFromBusket(profile);

       BusketInformation busketInformation = this.busketService.getBusketPrice(profile);

       ModelAndView modelAndView = new ModelAndView();
       modelAndView.addObject("products", products);
       modelAndView.addObject("reservationPrice", busketInformation.getPrice());
       modelAndView.addObject("timestamp", busketInformation.getReservation().getCreatedAt());
       modelAndView.setViewName("user/busket");

       return modelAndView;

   }

}
