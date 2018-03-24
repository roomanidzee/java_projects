package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.Reservation;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ReservationServiceInterface {

    List<Reservation> getReservationByCookie(HttpServletRequest req, HttpServletResponse resp);
    List<Reservation> getReservations();

    Reservation findById(Long id);

    void addReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void deleteReservation(Long id);

    void showReservation(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);
}
