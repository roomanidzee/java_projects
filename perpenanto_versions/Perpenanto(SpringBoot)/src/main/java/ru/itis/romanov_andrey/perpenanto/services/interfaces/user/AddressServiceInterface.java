package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.AddressToUser;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressServiceInterface {

    List<Address> getAddresses();
    List<AddressToUser> getAddressToUsers();
    List<AddressToUser> getAddressToUsersByCookie(String cookieValue);
    void saveOrUpdate(Address address);
    void delete(Long id);

}
