package com.romanidze.perpenanto.services.implementations;

import com.romanidze.perpenanto.dao.implementations.BusketDAOImpl;
import com.romanidze.perpenanto.dao.interfaces.BusketDAOInterface;
import com.romanidze.perpenanto.models.Busket;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.Reservation;
import com.romanidze.perpenanto.services.interfaces.BusketServiceInterface;
import com.romanidze.perpenanto.utils.DBConnection;
import com.romanidze.perpenanto.utils.ReservationStatus;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class BusketServiceImpl implements BusketServiceInterface{

    private ServletContext ctx;

    private BusketServiceImpl(){}

    public BusketServiceImpl(ServletContext ctx){

        this.ctx = ctx;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Integer getPriceForProducts(Long busketId) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        int result = 0;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            BusketDAOInterface busketDAO = new BusketDAOImpl(conn);

            Busket busket = busketDAO.find(busketId);
            List<Product> products = busket.getProducts();

            result = products.stream()
                             .mapToInt(Product::getPrice)
                             .sum();

        }catch(SQLException e){

            e.printStackTrace();

        }

        return result;

    }

    @Override
    public List<Product> getProductsFromBusket(Busket busket) {
        return busket.getProducts();
    }

    @Override
    public void payForOrder(Busket model, Reservation reservation, HttpServletResponse resp) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            BusketDAOInterface busketDAO = new BusketDAOImpl(conn);
            busketDAO.payForBusket(model, reservation);
            resp.sendRedirect("/user/reservation");

        }catch(SQLException | IOException e){

            e.printStackTrace();

        }
    }

    @Override
    public Map<Busket, Reservation> showAllFromBusket(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        HttpSession session = req.getSession(true);
        Long userId = (Long)session.getAttribute("user_id");

        Busket busket = null;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

             BusketDAOInterface busketDAO = new BusketDAOImpl(conn);
             busket = busketDAO.findByUser(userId);

        }catch(SQLException e){
            e.printStackTrace();
        }

        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime = LocalDateTime.now(moscowZone);

        Reservation reservation = Reservation.builder()
                                             .createdAt(Timestamp.valueOf(localDateTime))
                                             .status(String.valueOf(ReservationStatus.PREPARING))
                                             .build();

        context.setVariable("products", busket.getProducts());
        context.setVariable("reservationPrice", getPriceForProducts(busket.getId()));
        context.setVariable("timestamp", reservation.getCreatedAt());

        try{
            engine.process("busket.html", context, resp.getWriter());
        }catch(IOException e){
            e.printStackTrace();
        }

        Map<Busket, Reservation> resultMap = new HashMap<>();
        resultMap.put(busket, reservation);
        return resultMap;

    }
}
