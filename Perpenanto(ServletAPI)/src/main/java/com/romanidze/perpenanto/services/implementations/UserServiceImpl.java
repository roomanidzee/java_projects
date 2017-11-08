package com.romanidze.perpenanto.services.implementations;

import com.google.common.collect.Lists;
import com.romanidze.perpenanto.dao.implementations.AddressToUserDAOImpl;
import com.romanidze.perpenanto.dao.implementations.UserDAOImpl;
import com.romanidze.perpenanto.dao.interfaces.AddressToUserDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.UserDAOInterface;
import com.romanidze.perpenanto.models.Profile;
import com.romanidze.perpenanto.models.AddressToUser;
import com.romanidze.perpenanto.models.User;
import com.romanidze.perpenanto.services.interfaces.UserServiceInterface;
import com.romanidze.perpenanto.utils.DBConnection;
import com.romanidze.perpenanto.utils.WorkWithCookie;
import com.romanidze.perpenanto.utils.ProfileUtils;
import com.romanidze.perpenanto.utils.comparators.CompareAttributes;
import com.romanidze.perpenanto.utils.comparators.StreamCompareAttributes;
import org.mindrot.jbcrypt.BCrypt;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class UserServiceImpl implements UserServiceInterface{

    private ServletContext ctx;

    private UserServiceImpl(){}

    public UserServiceImpl(ServletContext ctx){

        this.ctx = ctx;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loginUser(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);

            String email = req.getParameter("email");
            String password = req.getParameter("password");
            boolean remember = req.getParameter("remember_me").equals("1");

            HttpSession session = req.getSession(true);

            WorkWithCookie cookieWork = new WorkWithCookie();

            cookieWork.createRememberCookie(resp, remember);

            User user = userDAO.findByUsername(email);

            if(remember){

                cookieWork.createCookie(resp, "remember_user",
                        BCrypt.hashpw(String.valueOf(user.getId()), BCrypt.gensalt()));

            }

            if(BCrypt.checkpw(password, user.getPassword())){

                session.setAttribute("user_id", user.getId());
                try {
                    resp.sendRedirect("/user/profile");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{

                context.setVariable("message", "Вы неправильно ввели логин, или пароль. Введите заново");

                try {
                    engine.process("login.html", context, resp.getWriter());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loginAdmin(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            HttpSession session = req.getSession(true);

            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User user = userDAO.findByUsername(username);

            if(password.equals(user.getPassword())){

                session.setAttribute("admin_id", user.getId());

                try{
                    resp.sendRedirect("/admin");
                }catch(IOException e){
                    e.printStackTrace();
                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registerUser(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context){

        ProfileUtils utils = new ProfileUtils();
        ProfileServiceImpl profileService = new ProfileServiceImpl(this.ctx);

        String email = req.getParameter("email");
        String plainPass = req.getParameter("password");
        String repeatPass = req.getParameter("repeat_pass");
        String personName = req.getParameter("person_name");
        String personSurname = req.getParameter("person_surname");
        String country = req.getParameter("countries");
        int postalCode = Integer.valueOf(req.getParameter("postal_code"));
        String city = req.getParameter("city");
        String street = req.getParameter("street");
        int homeNumber = Integer.valueOf(req.getParameter("home_number"));

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            AddressToUserDAOInterface addressToUserDAO = new AddressToUserDAOImpl(conn);

            boolean checkIfEmpty = !email.isEmpty() && !plainPass.isEmpty()
                                                    && !country.isEmpty()
                                                    && !personName.isEmpty()
                                                    && !personSurname.isEmpty()
                                                    && !(postalCode == 0)
                                                    && !city.isEmpty()
                                                    && !street.isEmpty()
                                                    && !(homeNumber == 0);

            if(!checkIfEmpty){

                boolean emailCheck = utils.checkEmail(email);

                if(emailCheck){

                    String message1 = "Введите корректный адрес электронной почты";

                    context.setVariable("message", message1);

                    try {
                        engine.process("registration.html", context, resp.getWriter());

                        resp.sendRedirect("/register");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                boolean countryCheck = utils.checkCountry(country);

                if(countryCheck){

                    String message2 = "Выберите страну из выпадающего списка";

                    context.setVariable("message", message2);

                    try {
                        engine.process("registration.html", context, resp.getWriter());

                        resp.sendRedirect("/register");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                User user = User.builder()
                                .emailOrUsername(email)
                                .password(BCrypt.hashpw(plainPass, BCrypt.gensalt()))
                                .build();

                if(!BCrypt.checkpw(repeatPass, user.getPassword())){

                    String message4 = "Пароли не совпадают";

                    context.setVariable("message", message4);

                    try {
                        engine.process("registration.html", context, resp.getWriter());

                        resp.sendRedirect("/register");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                userDAO.save(user);

                AddressToUser addressToUser = AddressToUser.builder()
                                                           .userId(user.getId())
                                                           .country(country)
                                                           .postalCode(postalCode)
                                                           .city(city)
                                                           .street(street)
                                                           .homeNumber(homeNumber)
                                                           .build();

                addressToUserDAO.save(addressToUser);

                Profile profile = Profile.builder()
                                         .userId(user.getId())
                                         .personName(personName)
                                         .personSurname(personSurname)
                                         .addressToUsers(Lists.newArrayList())
                                         .build();

                profile.getAddressToUsers().add(addressToUser);

                profileService.addProfile(profile);

                Long userId = user.getId();

                HttpSession session = req.getSession(true);
                session.setAttribute("user_id", userId);

                try {
                    resp.sendRedirect("/user/profile");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{

                context.setVariable("message", "Заполните все поля");

                try {
                    engine.process("registration.html", context, resp.getWriter());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }



    }

    @Override
    public User findById(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        User user = null;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            user = userDAO.find(id);

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }

    @Override
    public void addUser(User user) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                         configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            userDAO.save(user);

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateUser(User user) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            userDAO.update(user);

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteUser(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            userDAO.delete(id);

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<User> getUsers() {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());
        List<User> result = null;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            result = userDAO.findAll();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<User> getUsersByCookie(HttpServletRequest req, HttpServletResponse resp) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        WorkWithCookie cookieWork = new WorkWithCookie();

        List<User> sortedUsers = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            UserDAOInterface userDAO = new UserDAOImpl(conn);
            List<User> currentUsers = userDAO.findAll();

            int size = 3;

            Function<User, String> zero = (User u) -> String.valueOf(u.getId());
            Function<User, String> first = User::getEmailOrUsername;
            Function<User, String> second = User::getPassword;
            Cookie cookie = cookieWork.getCookieWithType(req, resp);

            List<Function<User, String>> functions = Arrays.asList(zero, first, second);
            List<String> indexes = Arrays.asList("0", "1", "2");

            Map<String, Function<User, String>> functionMap = new HashMap<>();

            IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

            CompareAttributes<User> compareAttr = new StreamCompareAttributes<>();
            sortedUsers.addAll(compareAttr.sortList(currentUsers, functionMap, cookie.getValue()));

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return sortedUsers;

    }

}
