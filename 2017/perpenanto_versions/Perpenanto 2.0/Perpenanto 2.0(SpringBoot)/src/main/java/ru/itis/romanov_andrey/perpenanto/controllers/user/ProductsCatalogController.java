package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.AuthenticationService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;

import java.util.List;

/**
 * 02.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class ProductsCatalogController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private BusketService busketService;

    @Autowired
    private AuthenticationService authenticationService;

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
        Profile profile = this.profileService.findByUserId(user.getId());
        Integer productsInBusketCount = this.busketService.getProductsCount(profile);
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
        Profile profile = this.profileService.findByUserId(user.getId());
        this.busketService.addProductToBusket(profile, productId);

        return new ModelAndView("redirect:/user/catalog");

    }

}
