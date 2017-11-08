package com.romanidze.perpenanto.services.implementations;

import com.romanidze.perpenanto.dao.implementations.BusketDAOImpl;
import com.romanidze.perpenanto.dao.implementations.ProductDAOImpl;
import com.romanidze.perpenanto.dao.interfaces.BusketDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ProductDAOInterface;
import com.romanidze.perpenanto.models.Busket;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.services.interfaces.ProductServiceInterface;
import com.romanidze.perpenanto.utils.DBConnection;
import com.romanidze.perpenanto.utils.WorkWithCookie;
import com.romanidze.perpenanto.utils.comparators.CompareAttributes;
import com.romanidze.perpenanto.utils.comparators.StreamCompareAttributes;
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

public class ProductServiceImpl implements ProductServiceInterface{

    private ServletContext ctx;

    private ProductServiceImpl(){}

    public ProductServiceImpl(ServletContext ctx){

        this.ctx = ctx;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Product> getProductsByCookie(HttpServletRequest req, HttpServletResponse resp) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        WorkWithCookie cookieWork = new WorkWithCookie();

        List<Product> sortedProducts = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            List<Product> currentProducts = productDAO.findAll();

            int size = 3;

            Function<Product, String> zero = (Product p) -> String.valueOf(p.getId());
            Function<Product, String> first = Product::getTitle;
            Function<Product, String> second = (Product p) -> String.valueOf(p.getPrice());

            List<Function<Product, String>> functions = Arrays.asList(zero, first, second);
            List<String> indexes = Arrays.asList("0", "1", "2");
            Cookie cookie = cookieWork.getCookieWithType(req, resp);

            Map<String, Function<Product, String>> functionMap = new HashMap<>();

            IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

            CompareAttributes<Product> compareAttr = new StreamCompareAttributes<>();

            sortedProducts.addAll(compareAttr.sortList(currentProducts, functionMap, cookie.getValue()));

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return sortedProducts;
    }

    @Override
    public List<Product> getProducts() {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        List<Product> result = null;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            result = productDAO.findAll();

        }catch(SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void addProduct(Product product) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            productDAO.save(product);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateProduct(Product product) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            productDAO.update(product);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteProduct(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            productDAO.delete(id);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public Product findById(Long id) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        Product product = null;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            product = productDAO.find(id);

        }catch(SQLException e){
            e.printStackTrace();
        }

        return product;

    }

    @Override
    public void showProductsCatalog(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context) {

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        List<Product> products = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            ProductDAOInterface productDAO = new ProductDAOImpl(conn);
            products.addAll(productDAO.findAll());

        }catch(SQLException e){
            e.printStackTrace();
        }

        context.setVariable("products", products);

        try{
            engine.process("products_catalog.html", context, resp.getWriter());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void addProductToBusket(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context) {

        HttpSession session = req.getSession(true);
        Long userId = (Long) session.getAttribute("user_id");

        Long id = Long.valueOf(req.getParameter("product_id"));
        String title = req.getParameter("product_title");
        Integer price = Integer.valueOf(req.getParameter("product_price"));
        String description = req.getParameter("product_description");
        String photoLink = req.getParameter("product_photoLink");

        Product product = Product.builder()
                                 .id(id)
                                 .title(title)
                                 .price(price)
                                 .description(description)
                                 .photoLink(photoLink)
                                 .build();

        DBConnection dbConnection = new DBConnection(this.ctx.getResourceAsStream("/WEB-INF/properties/db.properties"));

        Map<String, String> configMap = new LinkedHashMap<>();
        configMap.putAll(dbConnection.getDBConfig());

        Busket busket = null;

        try(Connection conn = DriverManager.getConnection(configMap.get("db_url"), configMap.get("db_username"),
                                                          configMap.get("db_password"))){

            BusketDAOInterface busketDAO = new BusketDAOImpl(conn);

            busket = busketDAO.findByUser(userId);
            busket.getProducts().add(product);
            busketDAO.update(busket);

        }catch(SQLException e){
            e.printStackTrace();
        }

        context.setVariable("productsCount", busket.getProducts().size());

        try{
            engine.process("products_catalog.html", context, resp.getWriter());
            resp.sendRedirect("/catalog");
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
