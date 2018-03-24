package ru.itis.romanov_andrey.perpenanto.services.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.Address;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.AddressToUser;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressServiceInterface {

    Address convertToAddress(AddressToUser address);
    List<Address> getAddresses();
    default List<Address> getAddressByCookie(String cookieValue){return null;}
    List<AddressToUser> getAddressToUsers();
    List<AddressToUser> getAddressToUsersByCookie(String cookieValue);
    void saveOrUpdate(Address address);
    void delete(Long id);

}
