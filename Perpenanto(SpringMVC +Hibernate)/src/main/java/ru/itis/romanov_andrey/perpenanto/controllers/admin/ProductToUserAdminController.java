package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.Product;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ProductToUser;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.ProductServiceInterface;

import java.util.List;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("ProductToUser Admin Controller Annotation")
public class ProductToUserAdminController {

    @Autowired
    private ProductServiceInterface productService;

    @GetMapping("/admin/product_infos")
    public ModelAndView getSortedProductInfos(@CookieValue(value = "status", defaultValue = "-1") String cookieValue) {

        List<ProductToUser> productInfos = this.productService.getProductsToUserByCookie(cookieValue);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product_infos", productInfos);
        modelAndView.setViewName("admin/product_info_admin");

        return modelAndView;

    }

    @PostMapping(value = "/admin/product_to_users", params = "form_action")
    public ModelAndView workWithProductToUsers(@RequestParam("form_action") String action,
                                               @ModelAttribute ProductToUser productToUser) {

        Product product = this.productService.convertToProduct(productToUser);

        if (action.equals("add") || action.equals("update")) {
            this.productService.saveOrUpdate(product);
        } else if (action.equals("delete")) {
            this.productService.delete(product.getId());
        }

        List<ProductToUser> productInfos = this.productService.getProductsToUser();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product_infos", productInfos);
        modelAndView.setViewName("admin/product_info_admin");

        return modelAndView;

    }

}
