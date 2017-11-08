package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.Reservation;
import com.romanidze.perpenanto.services.implementations.ReservationServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ReservationServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "ReservationAdminController",
        description = "Заказы пользователей",
        urlPatterns = {"/admin/reservations"}
)
public class ReservationAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ReservationServiceInterface reservationService = new ReservationServiceImpl(req.getServletContext());

        List<Reservation> reservations = reservationService.getReservationByCookie(req, resp);
        context.setVariable("reservations",reservations);

        try{
            engine.process("admin/reservations_admin.html", context, resp.getWriter());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        String status = req.getParameter("status");
        Timestamp createdAt = Timestamp.valueOf(req.getParameter("created_at"));

        String action = req.getParameter("form_action");

        ReservationServiceInterface reservationService = new ReservationServiceImpl(req.getServletContext());

        Reservation reservation = Reservation.builder()
                                             .id(id)
                                             .status(status)
                                             .createdAt(createdAt)
                                             .build();

        switch(action){

            case "add":
                reservationService.addReservation(reservation);
                break;

            case "update":
                reservationService.updateReservation(reservation);
                break;

            case "delete":
                reservationService.deleteReservation(reservation.getId());
                break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<Reservation> reservations = reservationService.getReservations();
        context.setVariable("reservations",reservations);

        try{
            engine.process("admin/reservations_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/reservations");
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
