package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketServiceInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 06.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Busket Controller")
public class BusketController {

    @Autowired
    private BusketServiceInterface busketService;

    @Autowired
    private AuthenticationServiceInterface authenticationService;

    @GetMapping("/user/busket")
    public ModelAndView getProductsFromBusket(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        List<Product> products = this.busketService.getProductsFromBusket(user.getProfile());

        Map<Reservation, Integer> orderMap = new HashMap<>();
        orderMap.putAll(this.busketService.getBusketPrice(user.getProfile()));
        Map.Entry<Reservation, Integer> entry = orderMap.entrySet().iterator().next();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("reservationPrice", entry.getValue());
        modelAndView.addObject("timestamp", entry.getKey().getCreatedAt());
        modelAndView.setViewName("user/busket");

        return modelAndView;

    }

}
