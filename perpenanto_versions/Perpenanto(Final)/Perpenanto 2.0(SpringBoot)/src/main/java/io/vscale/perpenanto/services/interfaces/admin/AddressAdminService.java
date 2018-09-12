package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.forms.admin.AddressToUserForm;

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
