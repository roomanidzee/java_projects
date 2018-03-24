package ru.itis.romanov_andrey.perpenanto.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ru.itis.romanov_andrey.perpenanto.forms.admin.*;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AddressAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductToUserAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProfileAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationInfoAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationToUserAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.UserAdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.AdminRequestCheck;

/**
 * 28.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller("Admin Statistics Change Controller")
@RequestMapping("/admin/statistics")
public class StatisticsChangeController {

    @Autowired
    private AdminRequestCheck adminRequestCheck;

    @Autowired
    private AddressAdminServiceInterface addressAdminService;

    @Autowired
    private ProductAdminServiceInterface productAdminService;

    @Autowired
    private ProductToUserAdminServiceInterface productToUserAdminService;

    @Autowired
    private ProfileAdminServiceInterface profileAdminService;

    @Autowired
    private ReservationAdminServiceInterface reservationAdminService;

    @Autowired
    private ReservationInfoAdminServiceInterface reservationInfoAdminService;

    @Autowired
    private ReservationToUserAdminServiceInterface reservationToUserAdminService;

    @Autowired
    private UserAdminServiceInterface userAdminService;

    @PostMapping(value = "/security", params = "form_action")
    public ModelAndView changeUsers(@RequestParam("form_action") String actionType,
                                    @ModelAttribute("userForm") UserForm userForm){


        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.userAdminService.addUser(userForm);
                    break;

                case "update":

                    this.userAdminService.updateUser(userForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/security");

    }

    @PostMapping(value = "/security", params = {"form_action", "id"})
    public ModelAndView deleteUser(@RequestParam("form_action") String actionType,
                                   @RequestParam("id") Long id){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.userAdminService.deleteUser(id);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/security");

    }

    @PostMapping(value = "/products", params = "form_action")
    public ModelAndView changeProducts(@RequestParam("form_action") String actionType,
                                       @ModelAttribute("productForm") ProductForm productForm){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.productAdminService.addProduct(productForm);
                    break;

                case "update":

                    this.productAdminService.updateProduct(productForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/products");

    }

    @PostMapping(value = "/products", params = {"form_action", "id"})
    public ModelAndView deleteProduct(@RequestParam("form_action") String actionType,
                                      @RequestParam("id") Long id){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.productAdminService.deleteProduct(id);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/products");

    }

    @PostMapping(value = "/profiles", params = "form_action")
    public ModelAndView changeProfiles(@RequestParam("form_action") String actionType,
                                       @ModelAttribute("profileForm") ProfileForm profileForm){


        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.profileAdminService.addProfile(profileForm);
                    break;

                case "update":

                    this.profileAdminService.updateProfile(profileForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/profiles");

    }

    @PostMapping(value = "/profiles", params = {"form_action", "id"})
    public ModelAndView deleteProfile(@RequestParam("form_action") String actionType,
                                      @RequestParam("id") Long id){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.profileAdminService.deleteProfile(id);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/profiles");

    }

    @PostMapping(value = "/reservations", params = "form_action")
    public ModelAndView changeReservations(@RequestParam("form_action") String actionType,
                                           @ModelAttribute("reservationForm") ReservationForm reservationForm){


        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.reservationAdminService.addReservation(reservationForm);
                    break;

                case "update":

                    this.reservationAdminService.updateReservation(reservationForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/reservations");

    }

    @PostMapping(value = "/reservations", params = {"form_action", "id"})
    public ModelAndView deleteReservation(@RequestParam("form_action") String actionType,
                                          @RequestParam("id") Long id){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.reservationAdminService.deleteReservation(id);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/reservations");

    }

    @PostMapping(value = "/addresses", params = "form_action")
    public ModelAndView changeAddresses(@RequestParam("form_action") String actionType,
                                        @ModelAttribute("addressForm") AddressToUserForm addressForm){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.addressAdminService.addAddress(addressForm);
                    break;

                case "update":

                    this.addressAdminService.updateAddress(addressForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/addresses");

    }

    @PostMapping(value = "/addresses", params = {"form_action", "userId", "postalCode"})
    public ModelAndView deleteAddress(@RequestParam("form_action") String actionType,
                                      @RequestParam("userId") Long userId,
                                      @RequestParam("postalCode") Integer postalCode){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.addressAdminService.deleteAddress(userId, postalCode);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/addresses");

    }

    @PostMapping(value = "/reservations_to_user", params = "form_action")
    public ModelAndView changeReservationsToUser(@RequestParam("form_action") String actionType,
                                                 @ModelAttribute("reservationToUserForm")
                                                  ReservationToUserForm reservationToUserForm){


        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.reservationToUserAdminService.addReservationToUser(reservationToUserForm);
                    break;

                case "update":

                    this.reservationToUserAdminService.updateReservationToUser(reservationToUserForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/reservations_to_user");

    }

    @PostMapping(value = "/reservations_to_user", params = {"form_action", "orderId"})
    public ModelAndView deleteReservationToUser(@RequestParam("form_action") String actionType,
                                                @RequestParam("orderId") Long orderId){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.reservationToUserAdminService.deleteReservationToUser(orderId);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/reservations_to_user");

    }

    @PostMapping(value = "/reservations_infos", params = "form_action")
    public ModelAndView changeReservationInfos(@RequestParam("form_action") String actionType,
                                               @ModelAttribute("reservationInfoForm")
                                                ReservationInfoForm reservationInfoForm){

       boolean check = this.adminRequestCheck.checkRequest(actionType);

       if(check){

           switch(actionType){

               case "add":

                   this.reservationInfoAdminService.addReservationInfo(reservationInfoForm);
                   break;

               case "update":

                   this.reservationInfoAdminService.updateReservationInfo(reservationInfoForm);
                   break;

           }

       }else{
           throw new NullPointerException("No such action!");
       }

       return new ModelAndView("redirect:/admin/statistics/reservations_infos");

    }

    @PostMapping(value = "/reservations_infos", params = {"form_action", "productId"})
    public ModelAndView deleteReservationInfo(@RequestParam("form_action") String actionType,
                                              @RequestParam("productId") Long productId){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.reservationInfoAdminService.deleteReservationInfo(productId);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/reservations_infos");

    }

    @PostMapping(value = "/products_to_user", params = "form_action")
    public ModelAndView changeProductsToUsers(@RequestParam("form_action") String actionType,
                                              @ModelAttribute("productToUserForm")
                                               ProductToUserForm productToUserForm){


        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){

            switch(actionType){

                case "add":

                    this.productToUserAdminService.addProductToUser(productToUserForm);
                    break;

                case "update":

                    this.productToUserAdminService.updateProductToUser(productToUserForm);
                    break;

            }

        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/products_to_user");

    }

    @PostMapping(value = "/products_to_user", params = {"form_action", "userId", "productId"})
    public ModelAndView deleteProductToUser(@RequestParam("form_action") String actionType,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("productId") Long productId){

        boolean check = this.adminRequestCheck.checkRequest(actionType);

        if(check){
            this.productToUserAdminService.deleteProductToUser(userId, productId);
        }else{
            throw new NullPointerException("No such action!");
        }

        return new ModelAndView("redirect:/admin/statistics/products_to_user");

    }

}
