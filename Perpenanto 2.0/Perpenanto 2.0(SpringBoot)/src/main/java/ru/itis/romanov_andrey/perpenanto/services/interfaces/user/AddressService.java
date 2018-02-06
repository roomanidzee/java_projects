package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;

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
