package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.AddressToUserDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.AddressService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressToUserRepository addressToUserRepository;

    @Autowired
    private UserRepository userRepository;

    private CompareAttributes<AddressToUserTransfer> compareAttributes = (oldList, functionMap, sortType) -> {

        List<AddressToUserTransfer> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                List<AddressToUser> addressToUsers = this.addressToUserRepository.findAll();
                EntityDTOInterface<AddressToUserTransfer, AddressToUser> entityDTO = new AddressToUserDTO();
                resultList.addAll(entityDTO.convert(addressToUsers));
                break;

            case "userId":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("userId")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "addressId":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("addressId")).reversed())
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<Address> getAddresses() {
        return this.addressRepository.findAll();
    }

    @Override
    public List<AddressToUserTransfer> getAddressToUsers() {
        EntityDTOInterface<AddressToUserTransfer, AddressToUser> entityDTO = new AddressToUserDTO();
        return entityDTO.convert(this.addressToUserRepository.findAll());
    }

    @Override
    public List<AddressToUserTransfer> getAddressToUsersByCookie(String cookieValue) {

        EntityDTOInterface<AddressToUserTransfer, AddressToUser> entityDTO = new AddressToUserDTO();
        List<AddressToUserTransfer> currentAddressToUsers = entityDTO.convert(this.addressToUserRepository.findAll());
        List<AddressToUserTransfer> sortedAddresses = new ArrayList<>();

        int size = 2;

        Function<AddressToUserTransfer, String> userIdFunction = address -> String.valueOf(address.getUserId());
        Function<AddressToUserTransfer, String> addressIdFunction = address -> String.valueOf(address.getAddressId());

        List<Function<AddressToUserTransfer, String>> functions = Arrays.asList(userIdFunction, addressIdFunction);
        List<String> sortTypes = Arrays.asList("userId", "addressId");

        Map<String, Function<AddressToUserTransfer, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEachOrdered(i ->{

            String sortType = sortTypes.get(i);
            Function<AddressToUserTransfer, String> function = functions.get(i);
            functionMap.put(sortType, function);

        });

        sortedAddresses.addAll(this.compareAttributes.sortList(currentAddressToUsers, functionMap, cookieValue));
        return sortedAddresses;

    }

    @Override
    public void saveAddress(AddressToUserTransfer address) {

        Address usualAddress = Address.builder()
                                      .country(address.getCountry())
                                      .postalCode(address.getPostalCode())
                                      .city(address.getCity())
                                      .street(address.getStreet())
                                      .homeNumber(address.getHomeNumber())
                                      .build();

        this.addressRepository.save(usualAddress);

        AddressToUser addressToUser = AddressToUser.builder()
                                                   .user(this.userRepository.find(address.getUserId()))
                                                   .addresses(Sets.newHashSet(usualAddress))
                                                   .build();

        this.addressToUserRepository.save(addressToUser);
    }

    @Override
    public void updateAddress(AddressToUserTransfer address) {

        Address usualAddress = Address.builder()
                                      .id(address.getAddressId())
                                      .country(address.getCountry())
                                      .postalCode(address.getPostalCode())
                                      .city(address.getCity())
                                      .street(address.getStreet())
                                      .homeNumber(address.getHomeNumber())
                                      .build();

        this.addressRepository.update(usualAddress);

        AddressToUser addressToUser = AddressToUser.builder()
                                                   .user(this.userRepository.find(address.getUserId()))
                                                   .addresses(Sets.newHashSet(usualAddress))
                                                   .build();

        this.addressToUserRepository.update(addressToUser);

    }

    @Override
    public void deleteAddress(AddressToUserTransfer address) {

        this.addressToUserRepository.delete(address.getUserId(), address.getAddressId());
        this.addressRepository.delete(address.getAddressId());

    }
}
