package ru.itis.romanov_andrey.perpenanto.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.AddressDAOInterface;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.UserDAOInterface;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.AddressToUserTransferImpl;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.AddressToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.Address;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.AddressServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;
import ru.itis.romanov_andrey.perpenanto.utils.StreamCompareAttributes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class AddressServiceImpl implements AddressServiceInterface {

    @Autowired
    private AddressDAOInterface addressDAO;

    @Autowired
    private UserDAOInterface userDAO;

    @Override
    public Address convertToAddress(AddressToUser address) {

        User user = this.userDAO.find(address.getUserId());
        Address address1 = this.addressDAO.findByPostalCode(address.getPostalCode());

        if(address1 == null){

            address1 = Address.builder()
                              .country(address.getCountry())
                              .postalCode(address.getPostalCode())
                              .city(address.getCity())
                              .street(address.getStreet())
                              .homeNumber(address.getHomeNumber())
                              .users(Collections.singletonList(user))
                              .build();

            this.addressDAO.save(address1);

        }

        return address1;

    }

    @Override
    public List<Address> getAddresses() {
        return this.addressDAO.findAll();
    }

    @Override
    public List<AddressToUser> getAddressToUsers() {

        AddressToUserTransferInterface addressToUserDTO = new AddressToUserTransferImpl();
        return addressToUserDTO.getAddressesToUsers(this.userDAO.findAll());

    }

    @Override
    public List<AddressToUser> getAddressToUsersByCookie(String cookieValue) {

        List<User> currentUsers = this.userDAO.findAll();
        List<AddressToUser> tempList = new ArrayList<>();
        List<AddressToUser> sortedList = new ArrayList<>();

        AddressToUserTransferInterface addressToUserDTO = new AddressToUserTransferImpl();
        tempList.addAll(addressToUserDTO.getAddressesToUsers(currentUsers));

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
        this.addressDAO.save(address);
    }

    @Override
    public void delete(Long id) {
        this.addressDAO.delete(id);
    }
}
