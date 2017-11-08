package com.romanidze.perpenanto.services.implementations;

import com.romanidze.perpenanto.dao.implementations.ReservationInfoDAOImpl;
import com.romanidze.perpenanto.dao.interfaces.ReservationInfoDAOInterface;
import com.romanidze.perpenanto.dto.implementations.ReservationInfoTransferImpl;
import com.romanidze.perpenanto.dto.interfaces.ReservationInfoTransferInterface;
import com.romanidze.perpenanto.models.ReservationInfo;
import com.romanidze.perpenanto.models.temp.TempReservationInfo;
import com.romanidze.perpenanto.services.interfaces.ReservationInfoServiceInterface;
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

public class ReservationInfoServiceImpl implements ReservationInfoServiceInterface{

    private ServletContext ctx;

    private ReservationInfoServiceImpl(){}

    public ReservationInfoServiceImpl(ServletContext ctx){

        this.ctx = ctx;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TempReservationInfo> getReservationInfosByCookie(HttpServletRequest req, HttpServletResponse resp) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        WorkWithCookie cookieWork = new WorkWithCookie();

        List<TempReservationInfo> tempList = new ArrayList<>();
        List<TempReservationInfo> sortedList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationInfoDAOInterface reservationInfoDAO = new ReservationInfoDAOImpl(conn);
            ReservationInfoTransferInterface reservationInfoDTO = new ReservationInfoTransferImpl();
            Cookie cookie = cookieWork.getCookieWithType(req, resp);

            tempList.addAll(reservationInfoDTO.getTempReservationInfos(reservationInfoDAO.findAll()));

            int size = 3;

            Function<TempReservationInfo, String> zero = (TempReservationInfo tri) -> String.valueOf(tri.getId());
            Function<TempReservationInfo, String> first = (TempReservationInfo tri) -> String.valueOf(tri.getUserProfileId());
            Function<TempReservationInfo, String> second = (TempReservationInfo tri) -> String.valueOf(tri.getUserReservationId());

            List<Function<TempReservationInfo, String>> functions = Arrays.asList(zero, first, second);
            List<String> indexes = Arrays.asList("0", "1", "2");

            Map<String, Function<TempReservationInfo, String>> functionMap = new HashMap<>();

            IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

            CompareAttributes<TempReservationInfo> compareAttr = new StreamCompareAttributes<>();
            sortedList.addAll(compareAttr.sortList(tempList, functionMap, cookie.getValue()));


        }catch(SQLException e){

            e.printStackTrace();

        }

        return sortedList;

    }

    @Override
    public void addReservationInfo(ReservationInfo reservationInfo) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationInfoDAOInterface reservationInfoDAO = new ReservationInfoDAOImpl(conn);
            reservationInfoDAO.save(reservationInfo);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateReservationInfo(ReservationInfo reservationInfo) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationInfoDAOInterface reservationInfoDAO = new ReservationInfoDAOImpl(conn);
            reservationInfoDAO.update(reservationInfo);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteReservationInfo(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationInfoDAOInterface reservationInfoDAO = new ReservationInfoDAOImpl(conn);
            reservationInfoDAO.delete(id);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public List<TempReservationInfo> getReservationInfos() {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        List<TempReservationInfo> resultList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ReservationInfoDAOInterface reservationInfoDAO = new ReservationInfoDAOImpl(conn);
            ReservationInfoTransferInterface reservationInfoDTO = new ReservationInfoTransferImpl();

            resultList.addAll(reservationInfoDTO.getTempReservationInfos(reservationInfoDAO.findAll()));

        }catch(SQLException e){
            e.printStackTrace();
        }

        return resultList;

    }
}
