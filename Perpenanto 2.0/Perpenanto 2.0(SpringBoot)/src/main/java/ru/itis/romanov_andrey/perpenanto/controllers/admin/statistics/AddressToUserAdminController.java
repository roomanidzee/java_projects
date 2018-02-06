package ru.itis.romanov_andrey.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.forms.admin.AddressToUserForm;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AddressAdminService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.AddressService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class AddressToUserAdminController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressAdminService addressAdminService;

    @GetMapping("/addresses")
    public ModelAndView getAddresses(@CookieValue(value = "status", defaultValue = "reset") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/address_to_user_admin");
        modelAndView.addObject("address_to_users",
                this.addressService.getAddressToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/addresses", params = "add_action")
    public ModelAndView saveNewAddress(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                       @ModelAttribute("addressForm") AddressToUserForm addressForm){

        this.addressAdminService.addAddress(addressForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/address_to_user_admin");
        modelAndView.addObject("address_to_users",
                this.addressService.getAddressToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/addresses", params = "update_action")
    public ModelAndView updateAddress(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                      @ModelAttribute("addressForm") AddressToUserForm addressForm){

        this.addressAdminService.updateAddress(addressForm);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/address_to_user_admin");
        modelAndView.addObject("address_to_users",
                this.addressService.getAddressToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @PostMapping(value = "/addresses", params = "delete_action")
    public ModelAndView deleteAddress(@CookieValue(value = "status", defaultValue = "reset") String cookieValue,
                                      @ModelAttribute("addressForm") AddressToUserForm addressForm){

        this.addressAdminService.deleteAddress(addressForm.getUserId(), addressForm.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/address_to_user_admin");
        modelAndView.addObject("address_to_users",
                this.addressService.getAddressToUsersByCookie(cookieValue));

        return modelAndView;

    }

}
