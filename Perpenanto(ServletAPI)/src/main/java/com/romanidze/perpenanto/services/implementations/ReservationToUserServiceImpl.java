package com.romanidze.perpenanto.services.implementations;

import com.romanidze.perpenanto.dao.implementations.ReservationToUserDAOImpl;
import com.romanidze.perpenanto.dao.interfaces.ReservationToUserDAOInterface;
import com.romanidze.perpenanto.dto.implementations.ReservationToUserTransferImpl;
import com.romanidze.perpenanto.dto.interfaces.ReservationToUserTransferInterface;
import com.romanidze.perpenanto.models.ReservationToUser;
import com.romanidze.perpenanto.models.temp.TempReservationToUser;
import com.romanidze.perpenanto.services.interfaces.ReservationToUserServiceInterface;
import com.romanidze.perpenanto.utils.DBConnection;
import com.romanidze.perpenanto.utils.WorkWithCookie;
import com.romanidze.perpenanto.utils.comparators.CompareAttributes;
import com.romanidze.perpenanto.utils.comparators.StreamCompareAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

public class ReservationToUserServiceImpl implements ReservationToUserServiceInterface{

    private ServletContext ctx;

    private ReservationToUserServiceImpl(){}

    public ReservationToUserServiceImpl(ServletContext ctx){

        this.ctx = ctx;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TempReservationToUser> getReservationToUserByCookie(HttpServletRequest req, HttpServletResponse resp) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        WorkWithCookie cookieWork = new WorkWithCookie();

        List<TempReservationToUser> tempList = new ArrayList<>();
        List<TempReservationToUser> sortedList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationToUserDAOInterface reservationToUserDAO = new ReservationToUserDAOImpl(conn);

            ReservationToUserTransferInterface reservationInfoDTO = new ReservationToUserTransferImpl();
            tempList.addAll(reservationInfoDTO.getTempReservationToUsers(reservationToUserDAO.findAll()));
            Cookie cookie = cookieWork.getCookieWithType(req, resp);

            int size = 3;

            Function<TempReservationToUser, String> zero = (TempReservationToUser trtu) -> String.valueOf(trtu.getId());
            Function<TempReservationToUser, String> first = (TempReservationToUser trtu) -> String.valueOf(trtu.getUserId());
            Function<TempReservationToUser, String> second = (TempReservationToUser trtu) -> String.valueOf(trtu.getUserReservationId());

            List<Function<TempReservationToUser, String>> functions = Arrays.asList(zero, first, second);
            List<String> indexes = Arrays.asList("0", "1", "2");

            Map<String, Function<TempReservationToUser, String>> functionMap = new HashMap<>();

            IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

            CompareAttributes<TempReservationToUser> compareAttr = new StreamCompareAttributes<>();
            sortedList.addAll(compareAttr.sortList(tempList, functionMap, cookie.getValue()));

        }catch(SQLException e){

            e.printStackTrace();

        }

        return sortedList;

    }

    @Override
    public List<TempReservationToUser> getReservationToUser() {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        List<TempReservationToUser> resultList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationToUserDAOInterface reservationToUserDAO = new ReservationToUserDAOImpl(conn);
            ReservationToUserTransferInterface reservationInfoDTO = new ReservationToUserTransferImpl();

            resultList.addAll(reservationInfoDTO.getTempReservationToUsers(reservationToUserDAO.findAll()));

        }catch(SQLException e){
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void addReservationToUser(ReservationToUser reservationToUser) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationToUserDAOInterface reservationToUserDAO = new ReservationToUserDAOImpl(conn);
            reservationToUserDAO.save(reservationToUser);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateReservationToUser(ReservationToUser reservationToUser) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationToUserDAOInterface reservationToUserDAO = new ReservationToUserDAOImpl(conn);
            reservationToUserDAO.update(reservationToUser);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteReservationToUser(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationToUserDAOInterface reservationToUserDAO = new ReservationToUserDAOImpl(conn);
            reservationToUserDAO.delete(id);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}
