package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.AddressToUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AddressToUserServiceInterface {

    List<AddressToUser> getAddressToUsersByCookie(HttpServletRequest req, HttpServletResponse resp);
    List<AddressToUser> getAddressToUsers();

    void addAddress(AddressToUser address);
    void updateAddress(AddressToUser address);
    void deleteAddress(Long id);
    AddressToUser findById(Long id);

}
