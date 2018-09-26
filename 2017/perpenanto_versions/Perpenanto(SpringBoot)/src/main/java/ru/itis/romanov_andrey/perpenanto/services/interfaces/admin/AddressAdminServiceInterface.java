package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.AddressToUserForm;

/**
 * 26.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressAdminServiceInterface {

    void addAddress(AddressToUserForm addressToUserForm);
    void updateAddress(AddressToUserForm addressToUserForm);
    void deleteAddress(Long userId, Integer postalCode);

}
