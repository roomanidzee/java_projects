package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.PDFSendService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductToReservationService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationService;

import java.sql.Timestamp;
import java.util.List;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ReservationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BusketService busketService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PDFSendService pdfSendService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductToReservationService productToReservationService;

    @PostMapping("/user/reservation")
    public ModelAndView getUserReservation(Authentication authentication,
                                           @RequestParam("timestamp")Timestamp timestamp){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());

        Reservation newReservation = Reservation.builder()
                                             .createdAt(timestamp)
                                             .reservationState(ReservationState.COLLECTING)
                                             .build();
        this.busketService.payForBusket(profile, newReservation);

        Reservation reservation = this.reservationService.getReservationByTimestamp(timestamp);
        List<Product> products = this.productToReservationService.getReservationProducts(user, timestamp);
        Integer price = this.productToReservationService.getReservationPrice(timestamp);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id", reservation.getId());
        modelAndView.addObject("products",products);
        modelAndView.addObject("reservationPrice", price);
        modelAndView.setViewName("user/reservation");

        this.pdfSendService.sendEmailWithPDF(user, timestamp);

        return modelAndView;

    }

}
