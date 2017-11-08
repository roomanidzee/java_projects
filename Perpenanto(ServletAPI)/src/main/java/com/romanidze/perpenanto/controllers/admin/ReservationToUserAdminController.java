package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.ReservationToUser;
import com.romanidze.perpenanto.models.temp.TempReservationToUser;
import com.romanidze.perpenanto.services.implementations.ReservationServiceImpl;
import com.romanidze.perpenanto.services.implementations.ReservationToUserServiceImpl;
import com.romanidze.perpenanto.services.implementations.UserServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ReservationServiceInterface;
import com.romanidze.perpenanto.services.interfaces.ReservationToUserServiceInterface;
import com.romanidze.perpenanto.services.interfaces.UserServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "ReservationToUserAdminController",
        description = "Заказы пользователей",
        urlPatterns = {"/admin/reservation_to_user"}
)
public class ReservationToUserAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ReservationToUserServiceInterface reservationToUserService = new ReservationToUserServiceImpl(req.getServletContext());

        List<TempReservationToUser> reservationToUsers = reservationToUserService.getReservationToUserByCookie(req, resp);
        context.setVariable("reservations_to_users", reservationToUsers);

        try{
            engine.process("admin/reservation_to_user_admin.html", context, resp.getWriter());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        Long userId = Long.valueOf(req.getParameter("user_id"));
        Long reservationId = Long.valueOf(req.getParameter("order_id"));

        String action = req.getParameter("form_action");

        ReservationToUserServiceInterface reservationToUserService = new ReservationToUserServiceImpl(req.getServletContext());
        UserServiceInterface userService = new UserServiceImpl(req.getServletContext());
        ReservationServiceInterface reservationService = new ReservationServiceImpl(req.getServletContext());

        ReservationToUser reservationToUser = ReservationToUser.builder()
                                                               .id(id)
                                                               .user(userService.findById(userId))
                                                               .userReservations(Collections.singletonList(
                                                                       reservationService.findById(reservationId)
                                                               ))
                                                               .build();

        switch(action){

            case "add":

                reservationToUserService.addReservationToUser(reservationToUser);
                break;

            case "update":

                reservationToUserService.updateReservationToUser(reservationToUser);
                break;

            case "delete":

                reservationToUserService.deleteReservationToUser(reservationToUser.getId());
                break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<TempReservationToUser> reservationToUsers = reservationToUserService.getReservationToUser();
        context.setVariable("reservations_to_users", reservationToUsers);

        try{
            engine.process("admin/reservation_to_user_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/reservation_to_user");
        }catch(IOException e){
            e.printStackTrace();
        }


    }

}
