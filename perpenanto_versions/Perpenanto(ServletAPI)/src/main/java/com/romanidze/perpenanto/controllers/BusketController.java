package com.romanidze.perpenanto.controllers;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.Busket;
import com.romanidze.perpenanto.models.Reservation;
import com.romanidze.perpenanto.services.implementations.BusketServiceImpl;
import com.romanidze.perpenanto.services.interfaces.BusketServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "BusketController",
        description = "Корзина пользователя",
        urlPatterns = {"/user/busket"}
)
public class BusketController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        BusketServiceInterface busketService = new BusketServiceImpl(req.getServletContext());
        Map<Busket, Reservation> propertyMap = busketService.showAllFromBusket(req, resp, engine, context);

        Map.Entry<Busket, Reservation> entry = propertyMap.entrySet().iterator().next();
        busketService.payForOrder(entry.getKey(), entry.getValue(), resp);

    }

}
