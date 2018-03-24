package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.Profile;
import com.romanidze.perpenanto.models.temp.TempProfile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProfileServiceInterface {

    void addProfile(Profile model);
    void updateProfile(Profile model);
    void deleteProfile(Long id);
    Profile findById(Long id);
    void showProfile(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);

    List<TempProfile> getProfilesByCookie(HttpServletRequest req, HttpServletResponse resp);
    List<TempProfile> getProfiles();
}
