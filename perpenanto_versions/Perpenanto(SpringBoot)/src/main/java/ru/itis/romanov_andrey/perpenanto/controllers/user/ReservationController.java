package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.SendPDFServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationServiceInterface;

import java.sql.Timestamp;
import java.util.List;

/**
 * 06.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ReservationController {

    @Autowired
    private ReservationServiceInterface reservationService;

    @Autowired
    private AuthenticationServiceInterface authenticationService;

    @Autowired
    private SendPDFServiceInterface sendPDFService;

    @PostMapping("/user/reservation")
    public ModelAndView getUserReservation(Authentication authentication,
                                           @RequestParam("timestamp")Timestamp timestamp){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Long id = this.reservationService.getReservationId(timestamp);
        List<Product> products = this.reservationService.getReservationProducts(user.getProfile(), timestamp);
        Integer price = this.reservationService.getReservationPrice(timestamp);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id",id);
        modelAndView.addObject("products",products);
        modelAndView.addObject("reservationPrice", price);
        modelAndView.setViewName("user/reservation");

        this.sendPDFService.sendEmailWithPDF(user, timestamp);

        return modelAndView;

    }

}
