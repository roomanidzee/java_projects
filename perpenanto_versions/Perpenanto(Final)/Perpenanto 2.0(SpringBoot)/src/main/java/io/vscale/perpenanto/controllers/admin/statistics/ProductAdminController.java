package io.vscale.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.forms.admin.ProductForm;
import io.vscale.perpenanto.services.interfaces.admin.ProductAdminService;
import io.vscale.perpenanto.services.interfaces.user.ProductService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class ProductAdminController {

    private final ProductService productService;
    private final ProductAdminService productAdminService;

    @Autowired
    public ProductAdminController(ProductService productService, ProductAdminService productAdminService) {
        this.productService = productService;
        this.productAdminService = productAdminService;
    }

    @GetMapping("/products")
    public ModelAndView getProducts(@CookieValue(value = "status", defaultValue = "reset") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/products_admin");
        modelAndView.addObject("products", this.productService.getProductsByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/products", params = "add_action")
    public ModelAndView saveNewProduct(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                       @ModelAttribute("productForm") ProductForm productForm){

        this.productAdminService.addProduct(productForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/products_admin");
        modelAndView.addObject("products", this.productService.getProductsByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/products", params = "update_action")
    public ModelAndView updateProduct(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                      @ModelAttribute("productForm") ProductForm productForm){

        this.productAdminService.updateProduct(productForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/products_admin");
        modelAndView.addObject("products", this.productService.getProductsByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/products", params = "delete_action")
    public ModelAndView deleteProduct(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                      @RequestParam("id") Long id){

        this.productAdminService.deleteProduct(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/products_admin");
        modelAndView.addObject("products", this.productService.getProductsByCookie(cookieValue));

        return modelAndView;

    }

}
