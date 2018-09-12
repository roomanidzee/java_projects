package io.vscale.perpenanto.controllers.user;

import io.vscale.perpenanto.utils.pagination.Page;
import io.vscale.perpenanto.utils.pagination.PageRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.services.interfaces.auth.AuthenticationService;
import io.vscale.perpenanto.services.interfaces.user.BusketService;
import io.vscale.perpenanto.services.interfaces.user.ProductService;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;

import java.util.List;

/**
 * 02.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsCatalogController {

    private ProductService productService;
    private ProfileService profileService;
    private BusketService busketService;
    private AuthenticationService authenticationService;

    @GetMapping("/catalog")
    public ModelAndView getCatalog(){

        ModelAndView modelAndView = new ModelAndView();
        List<Product> products = this.productService.getProducts();
        Long maxPrice = this.productService.getMaxPrice();

        modelAndView.addObject("products", products);
        modelAndView.addObject("maxPrice", maxPrice);
        modelAndView.setViewName("user/products_catalog");

        return modelAndView;

    }

    @RequestMapping(value = "/catalog_page", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getCatalogByPage(@ModelAttribute("pageRequest") PageRequest pageRequest){

        if( (pageRequest.getPageLimit() / 6 != 1) || (pageRequest.getPageOffset() / 6 != 1)  ){
            throw new IllegalArgumentException("Неверно заданы параметры запроса");
        }

        Page<Product> page = this.productService.getProductsByPage(pageRequest);
        Long productsCount = this.productService.countProducts();
        Long maxPrice = this.productService.getMaxPrice();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/products_catalog");
        modelAndView.addObject("page", page);
        modelAndView.addObject("maxPrice", maxPrice);
        modelAndView.addObject("limit", productsCount);

        return modelAndView;

    }

    @GetMapping("/user/catalog")
    public ModelAndView getCatalogWithBusket(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());
        Integer productsInBusketCount = this.busketService.getProductsCount(profile);
        List<Product> products = this.productService.getProducts();
        Long maxPrice = this.productService.getMaxPrice();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("products_count", productsInBusketCount);
        modelAndView.addObject("maxPrice", maxPrice);

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

    @PostMapping("/find_products_by_query")
    public ModelAndView getProductsByQuery(@RequestParam("userQuery") String userQuery){

        List<Product> products = this.productService.getProductsByQuery(userQuery);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.setViewName("redirect:/catalog");

        return modelAndView;

    }

    @PostMapping("/find_products_by_price_range")
    public ModelAndView getProductsByRange(@RequestParam("start") Integer start, @RequestParam("end") Integer end){

        List<Product> products = this.productService.getProductsByRange(start, end);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.setViewName("redirect:/catalog");

        return modelAndView;

    }

}
