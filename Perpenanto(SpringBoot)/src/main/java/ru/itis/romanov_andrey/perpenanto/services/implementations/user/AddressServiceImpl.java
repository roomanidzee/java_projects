package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.AddressToUserTransferImpl;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.AddressToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.AddressRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.AddressServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;
import ru.itis.romanov_andrey.perpenanto.utils.StreamCompareAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AddressServiceImpl implements AddressServiceInterface{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressToUserTransferInterface addressToUserDTO;

    @Override
    public List<Address> getAddresses() {
        return this.addressRepository.findAll();
    }

    @Override
    public List<AddressToUser> getAddressToUsers() {

        AddressToUserTransferInterface addressToUserDTO = new AddressToUserTransferImpl();
        return addressToUserDTO.getAddressesToUsers(this.userRepository.findAll());

    }

    @Override
    public List<AddressToUser> getAddressToUsersByCookie(String cookieValue) {

        List<User> currentUsers = this.userRepository.findAll();
        List<AddressToUser> tempList = new ArrayList<>();
        List<AddressToUser> sortedList = new ArrayList<>();
        tempList.addAll(this.addressToUserDTO.getAddressesToUsers(currentUsers));

        int size = 2;

        Function<AddressToUser, String> first = (AddressToUser address) -> String.valueOf(address.getUserId());
        Function<AddressToUser, String> second = (AddressToUser address) -> String.valueOf(address.getPostalCode());

        List<Function<AddressToUser, String>> functions = Arrays.asList(first, second);
        List<String> indexes = Arrays.asList("1", "2");

        Map<String, Function<AddressToUser, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<AddressToUser> compareAttr = new StreamCompareAttributes<>();
        sortedList.addAll(compareAttr.sortList(tempList, functionMap, cookieValue));

        return sortedList;

    }

    @Override
    public void saveOrUpdate(Address address) {
        this.addressRepository.save(address);
    }

    @Override
    public void delete(Long id) {
        this.addressRepository.delete(id);
    }
}
