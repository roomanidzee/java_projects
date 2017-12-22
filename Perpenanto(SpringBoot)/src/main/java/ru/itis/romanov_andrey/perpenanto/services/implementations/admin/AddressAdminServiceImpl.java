package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.AddressToUserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.AddressRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AddressAdminServiceInterface;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 26.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AddressAdminServiceImpl implements AddressAdminServiceInterface{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addAddress(AddressToUserForm addressToUserForm) {

        User user = this.userRepository.findOne(addressToUserForm.getUserId());

        Address address = Address.builder()
                                 .country(addressToUserForm.getCountry())
                                 .postalCode(addressToUserForm.getPostalCode())
                                 .city(addressToUserForm.getCity())
                                 .street(addressToUserForm.getStreet())
                                 .homeNumber(addressToUserForm.getHomeNumber())
                                 .users(Collections.singletonList(user))
                                 .build();

        this.addressRepository.save(address);
        user.getAddresses().add(address);

    }

    @Override
    public void updateAddress(AddressToUserForm addressToUserForm) {

        User user = this.userRepository.findOne(addressToUserForm.getUserId());
        List<User> users = this.userRepository.findAll();
        Address tempAddress = this.addressRepository.findOne(addressToUserForm.getId());

        if(tempAddress == null){
            throw new NullPointerException("Address not found!");
        }

        users.stream()
                .map(User::getAddresses)
                .forEachOrdered(addresses -> addresses.forEach(
                        tempAddressAgain -> {

                            if (tempAddressAgain.equals(tempAddress)) {

                                tempAddressAgain.setCountry(addressToUserForm.getCountry());
                                tempAddressAgain.setPostalCode(addressToUserForm.getPostalCode());
                                tempAddressAgain.setCity(addressToUserForm.getCity());
                                tempAddressAgain.setStreet(addressToUserForm.getStreet());
                                tempAddressAgain.setHomeNumber(addressToUserForm.getHomeNumber());
                                tempAddressAgain.setUsers(Collections.singletonList(user));

                                this.addressRepository.save(tempAddressAgain);
                            }

                        }
                ));

    }

    @Override
    public void deleteAddress(Long userId, Integer postalCode) {

        User tempUser = this.userRepository.findOne(userId);
        Address address = this.addressRepository.findByPostalCode(postalCode);

        Set<Address> addresses = tempUser.getAddresses();

        addresses.forEach(tempAddress ->{
            if(tempAddress.equals(address)){
                tempUser.getAddresses().remove(tempAddress);
                this.userRepository.save(tempUser);
            }
        });

    }
}
