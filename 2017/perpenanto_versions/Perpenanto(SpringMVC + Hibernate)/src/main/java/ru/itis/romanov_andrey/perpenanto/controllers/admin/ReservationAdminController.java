package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.Reservation;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.ReservationServiceInterface;

import java.util.List;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Reservations Admin Controller")
public class ReservationAdminController {

    @Autowired
    private ReservationServiceInterface reservationService;

    @GetMapping("/admin/reservations")
    public ModelAndView getSortedReservations(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        List<Reservation> reservations = this.reservationService.getReservationsByCookie(cookieValue);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reservations", reservations);
        modelAndView.setViewName("admin/reservations_admin");

        return modelAndView;

    }

    @PostMapping(value = "/admin/reservations", params = "form_action")
    public ModelAndView workWithReservations(@RequestParam("form_action") String action,
                                             @ModelAttribute Reservation reservation){

        if (action.equals("add") || action.equals("update")) {
            this.reservationService.saveOrUpdate(reservation);
        } else if (action.equals("delete")) {
            this.reservationService.delete(reservation.getId());
        }

        List<Reservation> reservations = this.reservationService.getReservations();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reservations", reservations);
        modelAndView.setViewName("admin/reservations_admin");

        return modelAndView;

    }

}
