package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.models.Address;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.AddressServiceInterface;

import java.util.List;

/**
 * 08.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("ProfileInfo Admin Controller")
public class AddressToUserAdminController {

    @Autowired
    private AddressServiceInterface addressService;

    @GetMapping("/admin/addresses")
    public ModelAndView getSortedAddresses(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        List<AddressToUser> addresses = this.addressService.getAddressToUsersByCookie(cookieValue);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("address_to_users", addresses);
        modelAndView.setViewName("admin/address_to_user_admin");

        return modelAndView;

    }

    @PostMapping(value = "/admin/addresses", params = "form_action")
    public ModelAndView workWithAddresses(@RequestParam("form_action") String action,
                                          @ModelAttribute Address addressToUser){

        if (action.equals("add") || action.equals("update")) {
            this.addressService.saveOrUpdate(addressToUser);
        } else if (action.equals("delete")) {
            this.addressService.delete(addressToUser.getId());
        }

        List<AddressToUser> addresses = this.addressService.getAddressToUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("address_to_users", addresses);
        modelAndView.setViewName("admin/address_to_user_admin");

        return modelAndView;

    }

}
