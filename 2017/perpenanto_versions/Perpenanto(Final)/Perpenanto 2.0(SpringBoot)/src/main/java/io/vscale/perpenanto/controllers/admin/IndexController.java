package io.vscale.perpenanto.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
public class IndexController {

    @GetMapping("/admin/index")
    public ModelAndView getIndexPage(){
        return new ModelAndView("admin/admin_page");
    }

}
