package io.vscale.perpenanto.controllers.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.security.states.ReservationState;
import io.vscale.perpenanto.services.interfaces.auth.AuthenticationService;
import io.vscale.perpenanto.services.interfaces.newsletter.PDFSendService;
import io.vscale.perpenanto.services.interfaces.user.BusketService;
import io.vscale.perpenanto.services.interfaces.user.ProductToReservationService;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;
import io.vscale.perpenanto.services.interfaces.user.ReservationService;

import java.sql.Timestamp;
import java.util.List;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReservationController {

    private AuthenticationService authenticationService;
    private BusketService busketService;
    private ProfileService profileService;
    private PDFSendService pdfSendService;
    private ReservationService reservationService;
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
