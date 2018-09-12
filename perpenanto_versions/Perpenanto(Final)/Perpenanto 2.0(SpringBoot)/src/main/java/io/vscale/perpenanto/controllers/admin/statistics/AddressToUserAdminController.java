package io.vscale.perpenanto.controllers.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.vscale.perpenanto.forms.admin.AddressToUserForm;
import io.vscale.perpenanto.services.interfaces.admin.AddressAdminService;
import io.vscale.perpenanto.services.interfaces.user.AddressService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/statistics")
public class AddressToUserAdminController {

    private final AddressService addressService;
    private final AddressAdminService addressAdminService;

    @Autowired
    public AddressToUserAdminController(AddressService addressService, AddressAdminService addressAdminService) {
        this.addressService = addressService;
        this.addressAdminService = addressAdminService;
    }

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
