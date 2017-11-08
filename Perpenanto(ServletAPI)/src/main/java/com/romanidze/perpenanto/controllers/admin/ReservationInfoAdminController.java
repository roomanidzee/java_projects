package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.ReservationInfo;
import com.romanidze.perpenanto.models.temp.TempReservationInfo;
import com.romanidze.perpenanto.services.implementations.ProductServiceImpl;
import com.romanidze.perpenanto.services.implementations.ProfileServiceImpl;
import com.romanidze.perpenanto.services.implementations.ReservationInfoServiceImpl;
import com.romanidze.perpenanto.services.implementations.ReservationServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ProductServiceInterface;
import com.romanidze.perpenanto.services.interfaces.ProfileServiceInterface;
import com.romanidze.perpenanto.services.interfaces.ReservationInfoServiceInterface;
import com.romanidze.perpenanto.services.interfaces.ReservationServiceInterface;
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
        name = "ReservationInfoAdminController",
        description = "Информация о заказах",
        urlPatterns = {"/admin/reservations_infos"}
)
public class ReservationInfoAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ReservationInfoServiceInterface reservationInfoService = new ReservationInfoServiceImpl(req.getServletContext());

        List<TempReservationInfo> reservationInfos = reservationInfoService.getReservationInfosByCookie(req, resp);
        context.setVariable("reservation_infos", reservationInfos);

        try{
            engine.process("admin/reservation_info_admin.html", context, resp.getWriter());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        Long userId = Long.valueOf(req.getParameter("user_id"));
        Long reservationId = Long.valueOf(req.getParameter("order_id"));
        Long productId = Long.valueOf(req.getParameter("product_id"));

        String action = req.getParameter("form_action");

        ReservationInfoServiceInterface reservationInfoService = new ReservationInfoServiceImpl(req.getServletContext());
        ProfileServiceInterface profileService = new ProfileServiceImpl(req.getServletContext());
        ReservationServiceInterface reservationService = new ReservationServiceImpl(req.getServletContext());
        ProductServiceInterface productService = new ProductServiceImpl(req.getServletContext());

        ReservationInfo reservationInfo = ReservationInfo.builder()
                                                         .id(id)
                                                         .userProfile(profileService.findById(userId))
                                                         .userReservation(reservationService.findById(reservationId))
                                                         .reservationProducts(Collections.singletonList(
                                                                                  productService.findById(productId)
                                                         ))
                                                         .build();

        switch(action){

            case "add":
                reservationInfoService.addReservationInfo(reservationInfo);
                break;

            case "update":
                reservationInfoService.updateReservationInfo(reservationInfo);
                break;

            case "delete":
                reservationInfoService.deleteReservationInfo(reservationInfo.getId());
                break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<TempReservationInfo> reservationInfos = reservationInfoService.getReservationInfos();

        context.setVariable("reservation_infos", reservationInfos);

        try{
            engine.process("admin/reservation_info_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/reservations_infos");
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
