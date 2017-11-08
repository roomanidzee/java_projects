package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.AddressToUser;
import com.romanidze.perpenanto.services.implementations.AddressToUserServiceImpl;
import com.romanidze.perpenanto.services.interfaces.AddressToUserServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(
        name = "AddressToUserAdminController",
        description = "Адреса пользователей",
        urlPatterns = {"/admin/addresses"}
)
public class AddressToUserAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        AddressToUserServiceInterface addressToUserService = new AddressToUserServiceImpl(req.getServletContext());

        List<AddressToUser> addresses = addressToUserService.getAddressToUsersByCookie(req, resp);
        context.setVariable("address_to_users", addresses);

        try{
            engine.process("admin/address_to_user_admin.html", context, resp.getWriter());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        Long userId = Long.valueOf(req.getParameter("user_id"));
        String country = req.getParameter("country");
        Integer postIndex = Integer.valueOf(req.getParameter("postIndex"));
        String city = req.getParameter("city");
        String street = req.getParameter("street");
        Integer homeNumber = Integer.valueOf("homeNumber");

        String action = req.getParameter("form_action");

        AddressToUser address = AddressToUser.builder()
                                             .id(id)
                                             .userId(userId)
                                             .country(country)
                                             .postalCode(postIndex)
                                             .city(city)
                                             .street(street)
                                             .homeNumber(homeNumber)
                                             .build();

        AddressToUserServiceInterface addressToUserService = new AddressToUserServiceImpl(req.getServletContext());

        switch(action){

            case "add":
               addressToUserService.addAddress(address);
               break;

            case "update":
               addressToUserService.updateAddress(address);
               break;

            case "delete":
               addressToUserService.deleteAddress(address.getId());
               break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<AddressToUser> addresses = addressToUserService.getAddressToUsers();
        context.setVariable("address_to_users", addresses);

        try{
            engine.process("admin/address_to_user_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/addresses");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
