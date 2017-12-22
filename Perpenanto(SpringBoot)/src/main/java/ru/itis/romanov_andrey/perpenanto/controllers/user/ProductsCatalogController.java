package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductServiceInterface;

import java.util.List;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Products Catalog Controller Annotation")
public class ProductsCatalogController {

    @Autowired
    private ProductServiceInterface productService;

    @Autowired
    private BusketServiceInterface busketService;

    @Autowired
    private AuthenticationServiceInterface authenticationService;

    @GetMapping("/catalog")
    public ModelAndView getCatalog(){

        ModelAndView modelAndView = new ModelAndView();
        List<Product> products = this.productService.getProducts();

        modelAndView.addObject("products", products);
        modelAndView.setViewName("user/products_catalog");

        return modelAndView;

    }

    @GetMapping("/user/catalog")
    public ModelAndView getCatalogWithBusket(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Integer productsInBusketCount = this.busketService.getProductsCount(user.getProfile());
        List<Product> products = this.productService.getProducts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("products_count", productsInBusketCount);

        modelAndView.setViewName("user/products_catalog");

        return modelAndView;
    }

    @PostMapping(value = "/user/catalog", params = "productId")
    public ModelAndView addProductToBusket(@RequestParam("productId") Long productId,
                                           Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        this.busketService.addProductToBusket(user.getProfile(), productId);

        return new ModelAndView("redirect:/user/catalog");

    }

}
