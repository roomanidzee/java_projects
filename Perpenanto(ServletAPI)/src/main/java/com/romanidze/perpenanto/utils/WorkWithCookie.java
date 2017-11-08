package com.romanidze.perpenanto.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WorkWithCookie {

    private String type = "status";

    public Cookie getCookieWithType(HttpServletRequest req, HttpServletResponse resp){

        List<Cookie> cookies = new ArrayList<>(Arrays.asList(req.getCookies()));

        Optional<Cookie> newResult = Optional.ofNullable(cookies.stream()
                                                                .filter(cookie -> cookie.getName().equals(this.type))
                                                                .findFirst()
                                                                .orElseGet(() -> {
                                                                    Cookie cookie = new Cookie(this.type, "-1");
                                                                    resp.addCookie(cookie);
                                                                    return cookie;
                                                                }));

        return newResult.get();

    }

    public void createRememberCookie(HttpServletResponse resp, boolean rememberCase){

        Cookie result;

        if(rememberCase){
            result = new Cookie("remember_user", "yes");
            result.setMaxAge(60 * 60 * 24 * 365);
        }else{
            result = new Cookie("remember_user","no");
        }

        resp.addCookie(result);
    }

    public void createCookie(HttpServletResponse resp, String name, String value){

        Cookie cookie = new Cookie(name, value);
        resp.addCookie(cookie);

    }

    public Optional<Cookie> getCookieByName(HttpServletRequest req, String name){

        List<Cookie> cookies = new ArrayList<>(Arrays.asList(req.getCookies()));

        return Optional.of(cookies.stream()
                                  .filter(cookie -> cookie.getName().equals(name))
                                  .findFirst()
                                  .orElseThrow(() -> new NullPointerException("Cookie not found")));

    }

}
