package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.Profile;
import com.romanidze.perpenanto.models.temp.TempProfile;
import com.romanidze.perpenanto.services.implementations.AddressToUserServiceImpl;
import com.romanidze.perpenanto.services.implementations.ProfileServiceImpl;
import com.romanidze.perpenanto.services.implementations.UserServiceImpl;
import com.romanidze.perpenanto.services.interfaces.AddressToUserServiceInterface;
import com.romanidze.perpenanto.services.interfaces.ProfileServiceInterface;
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
        name = "ProfileAdminController",
        description = "Профили пользователей",
        urlPatterns = {"/admin/profiles"}
)
public class ProfileAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ProfileServiceInterface profileService = new ProfileServiceImpl(req.getServletContext());

        List<TempProfile> profiles = profileService.getProfilesByCookie(req, resp);
        context.setVariable("profiles", profiles);

        try{
            engine.process("admin/profiles_admin.html", context, resp.getWriter());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("personName");
        String surname = req.getParameter("personSurname");
        Long userId = Long.valueOf(req.getParameter("user_id"));
        Long addressId = Long.valueOf(req.getParameter("address_id"));

        String action = req.getParameter("form_action");

        ProfileServiceInterface profileService = new ProfileServiceImpl(req.getServletContext());
        AddressToUserServiceInterface addressService = new AddressToUserServiceImpl(req.getServletContext());

        Profile profile = Profile.builder()
                                 .id(id)
                                 .personName(name)
                                 .personSurname(surname)
                                 .userId(userId)
                                 .addressToUsers(Collections.singletonList(addressService.findById(addressId)))
                                 .build();

        switch(action){

            case "add":
                profileService.addProfile(profile);
                break;

            case "update":
                profileService.updateProfile(profile);
                break;

            case "delete":
                profileService.deleteProfile(profile.getId());
                break;

        }

        List<TempProfile> profiles = profileService.getProfiles();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("profiles", profiles);

        try{
            engine.process("admin/profiles_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/profiles");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
