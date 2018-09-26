package io.vscale.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.forms.admin.ReservationForm;
import io.vscale.perpenanto.services.interfaces.admin.ReservationAdminService;
import io.vscale.perpenanto.services.interfaces.user.ReservationService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class ReservationAdminController {

    private final ReservationService reservationService;
    private final ReservationAdminService reservationAdminService;

    @Autowired
    public ReservationAdminController(ReservationService reservationService,
                                      ReservationAdminService reservationAdminService) {
        this.reservationService = reservationService;
        this.reservationAdminService = reservationAdminService;
    }

    @GetMapping("/reservations")
    public ModelAndView getReservations(@CookieValue(value = "status", defaultValue = "reset") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/reservations_admin");
        modelAndView.addObject("reservations", this.reservationService.getReservationsByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/reservations", params = "add_action")
    public ModelAndView saveNewReservation(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                           @ModelAttribute("reservationForm") ReservationForm reservationForm){

        this.reservationAdminService.addReservation(reservationForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/reservations_admin");
        modelAndView.addObject("reservations", this.reservationService.getReservationsByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/reservations", params = "update_action")
    public ModelAndView updateReservation(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                          @ModelAttribute("reservationForm") ReservationForm reservationForm){

        this.reservationAdminService.updateReservation(reservationForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/reservations_admin");
        modelAndView.addObject("reservations", this.reservationService.getReservationsByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/reservations", params = "delete_action")
    public ModelAndView deleteReservation(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                          @RequestParam("id") Long id){

        this.reservationAdminService.deleteReservation(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/reservations_admin");
        modelAndView.addObject("reservations", this.reservationService.getReservationsByCookie(cookieValue));

        return modelAndView;

    }

}
