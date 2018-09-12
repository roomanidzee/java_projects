package io.vscale.perpenanto.services.implementations.admin;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.admin.AddressToUserForm;
import io.vscale.perpenanto.models.usermodels.Address;
import io.vscale.perpenanto.models.usermodels.AddressToUser;
import io.vscale.perpenanto.repositories.interfaces.AddressRepository;
import io.vscale.perpenanto.repositories.interfaces.AddressToUserRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.services.interfaces.admin.AddressAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AddressAdminServiceImpl implements AddressAdminService{

    private final AddressRepository addressRepository;

    private final AddressToUserRepository addressToUserRepository;

    private final UserRepository userRepository;

    @Autowired
    public AddressAdminServiceImpl(AddressRepository addressRepository, AddressToUserRepository addressToUserRepository,
                                   UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.addressToUserRepository = addressToUserRepository;
        this.userRepository = userRepository;
    }

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
