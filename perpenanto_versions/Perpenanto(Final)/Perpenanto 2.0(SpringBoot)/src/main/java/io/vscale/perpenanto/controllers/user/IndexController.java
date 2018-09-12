package io.vscale.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.services.interfaces.user.ProductService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Index Controller Annotation")
public class IndexController {

    private final ProductService productService;

    @Autowired
    public IndexController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/index")
    public ModelAndView getIndex(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", this.productService.getRandomProducts());

        modelAndView.setViewName("user/index");

        return modelAndView;

    }

}
