package com.romanidze.perpenanto.services.implementations;

import com.romanidze.perpenanto.dao.implementations.ProductToUserDAOImpl;
import com.romanidze.perpenanto.dao.interfaces.ProductToUserDAOInterface;
import com.romanidze.perpenanto.dto.implementations.ProductToUserTransferImpl;
import com.romanidze.perpenanto.dto.interfaces.ProductToUserTransferInterface;
import com.romanidze.perpenanto.models.ProductToUser;
import com.romanidze.perpenanto.models.temp.TempProductToUser;
import com.romanidze.perpenanto.services.interfaces.ProductToUserServiceInterface;
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

public class ProductToUserServiceImpl implements ProductToUserServiceInterface{

    private ServletContext ctx;

    private ProductToUserServiceImpl(){}

    public ProductToUserServiceImpl(ServletContext ctx){

        this.ctx = ctx;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TempProductToUser> getProductToUserByCookie(HttpServletRequest req, HttpServletResponse resp) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        WorkWithCookie cookieWork = new WorkWithCookie();

        List<TempProductToUser> tempList = new ArrayList<>();
        List<TempProductToUser> sortedList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductToUserDAOInterface productToUserDAO = new ProductToUserDAOImpl(conn);
            List<ProductToUser> currentProductToUser = productToUserDAO.findAll();
            Cookie cookie = cookieWork.getCookieWithType(req, resp);

            ProductToUserTransferInterface productToUserDTO = new ProductToUserTransferImpl();
            tempList.addAll(productToUserDTO.getTempProductToUsers(currentProductToUser));

            int size = 3;

            Function<TempProductToUser, String> zero = (TempProductToUser tptu) -> String.valueOf(tptu.getId());
            Function<TempProductToUser, String> first = (TempProductToUser tptu) -> String.valueOf(tptu.getUserId());
            Function<TempProductToUser, String> second = (TempProductToUser tptu) -> String.valueOf(tptu.getProductId());

            List<Function<TempProductToUser, String>> functions = Arrays.asList(zero, first, second);
            List<String> indexes = Arrays.asList("0", "1", "2");

            Map<String, Function<TempProductToUser, String>> functionMap = new HashMap<>();

            IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

            CompareAttributes<TempProductToUser> compareAttr = new StreamCompareAttributes<>();
            sortedList.addAll(compareAttr.sortList(tempList, functionMap, cookie.getValue()));

        }catch(SQLException e){

            e.printStackTrace();

        }

        return sortedList;

    }

    @Override
    public List<TempProductToUser> getProducts() {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        List<TempProductToUser> resultList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductToUserDAOInterface productToUserDAO = new ProductToUserDAOImpl(conn);
            List<ProductToUser> currentProductToUser = productToUserDAO.findAll();

            ProductToUserTransferInterface productToUserDTO = new ProductToUserTransferImpl();
            resultList.addAll(productToUserDTO.getTempProductToUsers(currentProductToUser));

        }catch(SQLException e){
            e.printStackTrace();
        }

        return resultList;

    }

    @Override
    public void addProductToUser(ProductToUser productToUser) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductToUserDAOInterface productToUserDAO = new ProductToUserDAOImpl(conn);
            productToUserDAO.save(productToUser);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateProductToUser(ProductToUser productToUser) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                configMap.get("db_password"))){

            ProductToUserDAOInterface productToUserDAO = new ProductToUserDAOImpl(conn);
            productToUserDAO.update(productToUser);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteProductToUser(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                configMap.get("db_password"))){

            ProductToUserDAOInterface productToUserDAO = new ProductToUserDAOImpl(conn);
            productToUserDAO.delete(id);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}
