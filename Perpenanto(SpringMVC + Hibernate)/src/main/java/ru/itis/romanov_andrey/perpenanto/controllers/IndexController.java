package ru.itis.romanov_andrey.perpenanto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Index Controller Annotation")
public class IndexController {

    @GetMapping("/index")
    public ModelAndView getIndex(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("price1", 1500);
        modelAndView.addObject("price2", 4000);
        modelAndView.addObject("price3", 300);

        modelAndView.setViewName("index");

        return modelAndView;

    }

}
