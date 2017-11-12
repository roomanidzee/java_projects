package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.Product;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.ProductServiceInterface;

import java.util.List;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Product Admin Controller Annotation")
public class ProductAdminController {

    @Autowired
    private ProductServiceInterface productService;

    @GetMapping("/admin/products")
    public ModelAndView getSortedProducts(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        List<Product> products = this.productService.getProductsByCookie(cookieValue);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.setViewName("admin/products_admin");

        return modelAndView;

    }

    @PostMapping(value = "/admin/products", params = "form_action")
    public ModelAndView workWithProducts(@RequestParam("form_action") String action,
                                         @ModelAttribute Product product){

        if (action.equals("add") || action.equals("update")) {
            this.productService.saveOrUpdate(product);
        } else if (action.equals("delete")) {
            this.productService.delete(product.getId());
        }

        List<Product> products = this.productService.getProducts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.setViewName("admin/products_admin");

        return modelAndView;

    }

}
