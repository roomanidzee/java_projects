package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProductServiceInterface {

    List<Product> getProductsByCookie(HttpServletRequest req, HttpServletResponse resp);
    List<Product> getProducts();

    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Long id);

    Product findById(Long id);

    void showProductsCatalog(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);
    void addProductToBusket(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);

}
