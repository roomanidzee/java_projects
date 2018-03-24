package com.romanidze.perpenanto.controllers;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.services.implementations.ReservationServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ReservationServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "ReservationController",
        description = "Подтверждение заказа",
        urlPatterns = {"/user/reservation"}
)
public class ReservationController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ReservationServiceInterface reservationService = new ReservationServiceImpl(req.getServletContext());
        reservationService.showReservation(req, resp, engine, context);

    }

}
