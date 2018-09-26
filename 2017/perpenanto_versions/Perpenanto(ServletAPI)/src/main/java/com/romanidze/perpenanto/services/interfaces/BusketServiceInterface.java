package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.Busket;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.Reservation;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface BusketServiceInterface {

    Integer getPriceForProducts(Long busketId);
    List<Product> getProductsFromBusket(Busket busket);
    void payForOrder(Busket model, Reservation reservation, HttpServletResponse resp);

    Map<Busket, Reservation> showAllFromBusket(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);
}
