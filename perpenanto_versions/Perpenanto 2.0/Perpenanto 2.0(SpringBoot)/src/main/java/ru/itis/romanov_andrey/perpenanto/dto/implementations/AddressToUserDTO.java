package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 01.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class AddressToUserDTO implements EntityDTOInterface<AddressToUserTransfer, AddressToUser>{

    @Override
    public List<AddressToUserTransfer> convert(List<AddressToUser> originalList) {

        Map<Long, Address> userToAddressMap = new HashMap<>();

        originalList.forEach(addressToUser -> {

            User user = addressToUser.getUser();
            Set<Address> addresses = addressToUser.getAddresses();

            addresses.forEach(address -> userToAddressMap.put(user.getId(), address));

        });

        return userToAddressMap.entrySet()
                               .stream()
                               .map(entry -> AddressToUserTransfer.builder()
                                                                  .userId(entry.getKey())
                                                                  .addressId(entry.getValue().getId())
                                                                  .country(entry.getValue().getCountry())
                                                                  .postalCode(entry.getValue().getPostalCode())
                                                                  .city(entry.getValue().getCity())
                                                                  .street(entry.getValue().getStreet())
                                                                  .homeNumber(entry.getValue().getHomeNumber())
                                                                  .build()
                               )
                               .collect(Collectors.toList());

    }
}
