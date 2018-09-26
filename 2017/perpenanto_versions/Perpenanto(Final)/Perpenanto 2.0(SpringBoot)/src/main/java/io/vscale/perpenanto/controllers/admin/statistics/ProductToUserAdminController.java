package io.vscale.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.forms.admin.ProductToUserForm;
import io.vscale.perpenanto.services.interfaces.admin.ProductToUserAdminService;
import io.vscale.perpenanto.services.interfaces.user.ProductToUserService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class ProductToUserAdminController {

    private final ProductToUserService productToUserService;
    private final ProductToUserAdminService productToUserAdminService;

    @Autowired
    public ProductToUserAdminController(ProductToUserService productToUserService,
                                        ProductToUserAdminService productToUserAdminService) {
        this.productToUserService = productToUserService;
        this.productToUserAdminService = productToUserAdminService;
    }

    @GetMapping("/products_to_user")
    public ModelAndView getProductsToUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/product_to_user_admin");
        modelAndView.addObject("products_to_users",
                this.productToUserService.getProductsToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/products_to_user", params = "add_action")
    public ModelAndView saveNewProductToUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                             @ModelAttribute("productToUserForm") ProductToUserForm productToUserForm){

        this.productToUserAdminService.addProductToUser(productToUserForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/product_to_user_admin");
        modelAndView.addObject("products_to_users",
                this.productToUserService.getProductsToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/products_to_user", params = "update_action")
    public ModelAndView updateProductToUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                            @ModelAttribute("productToUserForm") ProductToUserForm productToUserForm){

        this.productToUserAdminService.updateProductToUser(productToUserForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/product_to_user_admin");
        modelAndView.addObject("products_to_users",
                this.productToUserService.getProductsToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/products_to_user", params = "delete_action")
    public ModelAndView deleteProductToUser(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                            @ModelAttribute("productToUserForm") ProductToUserForm productToUserForm){

        this.productToUserAdminService.deleteProductToUser(
                productToUserForm.getUserId(), productToUserForm.getProductId()
        );

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/product_to_user_admin");
        modelAndView.addObject("products_to_users",
                this.productToUserService.getProductsToUsersByCookie(cookieValue));

        return modelAndView;

    }

}
