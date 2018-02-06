package ru.itis.romanov_andrey.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductToUserForm;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductToUserAdminService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductToUserService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class ProductToUserAdminController {

    @Autowired
    private ProductToUserService productToUserService;

    @Autowired
    private ProductToUserAdminService productToUserAdminService;

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
