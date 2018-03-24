package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.AddressToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.Address;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.AddressToUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class AddressToUserTransferImpl implements AddressToUserTransferInterface {

    @Override
    public List<AddressToUser> getAddressesToUsers(List<User> users) {

        List<AddressToUser> resultList = new ArrayList<>();

        int listSize = users.size();

        IntStream.range(0, listSize).forEachOrdered(i -> {
            User user = users.get(i);
            Set<Address> addresses = user.getAddresses();
            addresses.stream()
                     .map(address -> AddressToUser.builder()
                                                  .userId(user.getId())
                                                  .country(address.getCountry())
                                                  .postalCode(address.getPostalCode())
                                                  .city(address.getCity())
                                                  .street(address.getStreet())
                                                  .homeNumber(address.getHomeNumber())
                                                  .build()
                     )
                     .forEachOrdered(resultList::add);
        });

        return resultList;

    }
}
