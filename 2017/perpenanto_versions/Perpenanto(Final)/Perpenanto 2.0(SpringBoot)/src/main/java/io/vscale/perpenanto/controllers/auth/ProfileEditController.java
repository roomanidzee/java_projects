package io.vscale.perpenanto.controllers.auth;

import io.vscale.perpenanto.forms.admin.AddressToUserForm;
import io.vscale.perpenanto.forms.admin.ProfileForm;
import io.vscale.perpenanto.forms.user.ProductCreateForm;
import io.vscale.perpenanto.models.transfermodels.AddressToUserTransfer;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.services.interfaces.auth.AuthenticationService;
import io.vscale.perpenanto.services.interfaces.files.FileStorageService;
import io.vscale.perpenanto.services.interfaces.user.AddressService;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;
import io.vscale.perpenanto.services.interfaces.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 06.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProfileEditController {

    private AuthenticationService authenticationService;
    private ProfileService profileService;
    private UserService userService;
    private AddressService addressService;
    private FileStorageService fileStorageService;

    @PostMapping("/send_photo")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file, Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());
        boolean sent = this.fileStorageService.saveFile(file, profile);

        if (sent) {
            return new ModelAndView("redirect:/admin/statistics/security");
        }
        throw new NullPointerException("Photo not sended!");

    }

    @PostMapping(value = "/profile", params = {"product_action"})
    public ModelAndView addProduct(@ModelAttribute("productForm") ProductCreateForm productForm,
                                   Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        this.userService.addProduct(user, productForm);
        return new ModelAndView("user/add_product");

    }

    @PostMapping(value = "/profile", params = "edit_action")
    public ModelAndView changeProfile(@ModelAttribute("profileForm") ProfileForm profileForm,
                                      Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Profile profile = this.profileService.findByUserId(user.getId());

        profile.setEmail(profileForm.getEmail());
        profile.setName(profileForm.getPersonName());
        profile.setSurname(profileForm.getPersonSurname());

        this.profileService.updateProfile(profile);
        return new ModelAndView("redirect:/profile");
    }

    @PostMapping(value = "/profile", params = "address_action")
    public ModelAndView addAddress(@ModelAttribute("addressForm") AddressToUserForm addressForm,
                                   Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);

        AddressToUserTransfer address = AddressToUserTransfer.builder()
                                                             .userId(user.getId())
                                                             .country(addressForm.getCountry())
                                                             .postalCode(addressForm.getPostalCode())
                                                             .city(addressForm.getCity())
                                                             .street(addressForm.getStreet())
                                                             .homeNumber(addressForm.getHomeNumber())
                                                             .build();
        this.addressService.saveAddress(address);
        return new ModelAndView("redirect:/profile");

    }

}
