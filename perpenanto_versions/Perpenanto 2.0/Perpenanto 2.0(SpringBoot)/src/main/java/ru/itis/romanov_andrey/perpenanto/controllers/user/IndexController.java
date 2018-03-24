package ru.itis.romanov_andrey.perpenanto.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Index Controller Annotation")
public class IndexController {

    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public ModelAndView getIndex(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", this.productService.getRandomProducts());

        modelAndView.setViewName("user/index");

        return modelAndView;

    }

}
