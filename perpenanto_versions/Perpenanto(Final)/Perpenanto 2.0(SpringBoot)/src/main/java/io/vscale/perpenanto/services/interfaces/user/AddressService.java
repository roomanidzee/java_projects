package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.models.transfermodels.AddressToUserTransfer;
import io.vscale.perpenanto.models.usermodels.Address;

import java.util.List;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressService {

    List<Address> getAddresses();
    List<AddressToUserTransfer> getAddressToUsers();
    List<AddressToUserTransfer> getAddressToUsersByCookie(String cookieValue);

    void saveAddress(AddressToUserTransfer address);
    void updateAddress(AddressToUserTransfer address);
    void deleteAddress(AddressToUserTransfer address);

}
