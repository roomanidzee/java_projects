package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.AddressToUserForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressAdminService {

    void addAddress(AddressToUserForm addressToUserForm);
    void updateAddress(AddressToUserForm addressToUserForm);
    void deleteAddress(Long userId, Long addressId);

}
