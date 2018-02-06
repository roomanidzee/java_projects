package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.AddressToUserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AddressAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AddressAdminServiceImpl implements AddressAdminService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressToUserRepository addressToUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addAddress(AddressToUserForm addressToUserForm) {

        Address address = Address.builder()
                                 .country(addressToUserForm.getCountry())
                                 .postalCode(addressToUserForm.getPostalCode())
                                 .city(addressToUserForm.getCity())
                                 .street(addressToUserForm.getStreet())
                                 .homeNumber(addressToUserForm.getHomeNumber())
                                 .build();
        this.addressRepository.save(address);

        AddressToUser addressToUser = AddressToUser.builder()
                                                   .user(this.userRepository.find(addressToUserForm.getUserId()))
                                                   .addresses(Sets.newHashSet(address))
                                                   .build();
        this.addressToUserRepository.save(addressToUser);

    }

    @Override
    public void updateAddress(AddressToUserForm addressToUserForm) {

        Address address = Address.builder()
                                 .id(addressToUserForm.getId())
                                 .country(addressToUserForm.getCountry())
                                 .postalCode(addressToUserForm.getPostalCode())
                                 .city(addressToUserForm.getCity())
                                 .street(addressToUserForm.getStreet())
                                 .homeNumber(addressToUserForm.getHomeNumber())
                                 .build();
        this.addressRepository.update(address);

        AddressToUser addressToUser = AddressToUser.builder()
                                                   .user(this.userRepository.find(addressToUserForm.getUserId()))
                                                   .addresses(Sets.newHashSet(address))
                                                   .build();
        this.addressToUserRepository.update(addressToUser);

    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {

        this.addressToUserRepository.delete(userId, addressId);
        this.addressRepository.delete(addressId);

    }
}
