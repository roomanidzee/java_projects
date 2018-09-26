package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.files.FileInfoServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.AddressServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationServiceInterface;

/**
 * 23.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Admin Statistics Controller")
@RequestMapping("/admin/statistics")
public class StatisticsController {

    @Autowired
    private AddressServiceInterface addressService;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private ProductServiceInterface productService;

    @Autowired
    private ProfileServiceInterface profileService;

    @Autowired
    private ReservationServiceInterface reservationService;

    @Autowired
    private FileInfoServiceInterface fileInfoService;

    @GetMapping(value = "/security")
    public ModelAndView getUsers(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/users_admin");
        modelAndView.addObject("users", this.userService.getUsersByRoleAndCookie(Role.USER, cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/products")
    public ModelAndView getProducts(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/products_admin");
        modelAndView.addObject("products", this.productService.getProductsByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/profiles")
    public ModelAndView getProfiles(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/profiles_admin");
        modelAndView.addObject("profiles", this.profileService.getProfilesByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/reservations")
    public ModelAndView getReservations(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/reservations_admin");
        modelAndView.addObject("reservations", this.reservationService.getReservationsByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/addresses")
    public ModelAndView getAddresses(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/address_to_user_admin");
        modelAndView.addObject("address_to_users",
                this.addressService.getAddressToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/reservations_to_user")
    public ModelAndView getReservationsToUser(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/reservation_to_user_admin");
        modelAndView.addObject("reservations_to_users",
                this.reservationService.getReservationToUsersByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/reservations_infos")
    public ModelAndView getReservationInfos(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/reservation_info_admin");
        modelAndView.addObject("reservation_infos",
                this.reservationService.getReservationInfosByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping(value = "/products_to_user")
    public ModelAndView getProductsToUser(@CookieValue(value = "status", defaultValue = "-1") String cookieValue){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/product_to_user_admin");
        modelAndView.addObject("products_to_users",
                this.productService.getProductsToUserByCookie(cookieValue));

        return modelAndView;

    }

    @GetMapping("/files")
    public ModelAndView getFilesToUser(){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/file_to_user_admin");
        modelAndView.addObject("files_to_user",
                this.fileInfoService.getFileToUser(this.fileInfoService.getFiles()));

        return modelAndView;

    }
}
