package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.AddressToUserDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.forms.user.ReservationInformation;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.*;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.*;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
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
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductToUserRepository productToUserRepository;

    @Autowired
    private AddressToUserRepository addressToUserRepository;

    @Autowired
    private ProductToReservationRepository productToReservationRepository;

    @Autowired
    private ReservationToUserRepository reservationToUserRepository;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy (HH:mm:ss)");

    private CompareAttributes<Profile> compareAttributes = (oldList, functionMap, sortType) -> {

        List<Profile> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                resultList.addAll(this.profileRepository.findAll());
                break;

            case "id":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("id")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "name":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("name")))
                                    .collect(Collectors.toList());
                break;

            case "surname":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("surname")))
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<Profile> getProfiles() {
        return this.profileRepository.findAll();
    }

    @Override
    public List<Profile> getProfilesByCookie(String cookieValue) {

        List<Profile> currentProfiles = this.profileRepository.findAll();
        List<Profile> sortedProfiles = new ArrayList<>();

        int size = 3;

        Function<Profile, String> idFunction = profile -> String.valueOf(profile.getId());
        Function<Profile, String> nameFunction = Profile::getName;
        Function<Profile, String> surnameFunction = Profile::getSurname;

        List<Function<Profile, String>> functions = Arrays.asList(idFunction, nameFunction, surnameFunction);
        List<String> sortTypes = Arrays.asList("id", "name", "surname");

        Map<String, Function<Profile, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEachOrdered(i ->{

            String sortType = sortTypes.get(i);
            Function<Profile, String> function = functions.get(i);
            functionMap.put(sortType, function);

        });

        sortedProfiles.addAll(this.compareAttributes.sortList(currentProfiles, functionMap, cookieValue));
        return sortedProfiles;

    }

    @Override
    public void saveProfile(Profile profile) {
        this.profileRepository.save(profile);
    }

    @Override
    public void updateProfile(Profile profile) {
        this.profileRepository.update(profile);
    }

    @Override
    public void deleteProfile(Profile profile) {
        this.profileRepository.delete(profile.getId());
    }

    @Override
    public Profile findByUserId(Long userId) {
        User user = this.userRepository.find(userId);
        return this.profileRepository.findByUser(user);
    }

    @Override
    public Set<Product> getProductsByUser(User user) {
        ProductToUser productToUser;

        try{
            productToUser =
             DataAccessUtils.objectResult(Collections.singletonList(this.productToUserRepository.find(user.getId())),
                                          ProductToUser.class);
        }catch(IncorrectResultSizeDataAccessException e){
            return null;
        }

        return productToUser.getProducts();
    }

    @Override
    public Integer countReservations(User user) {
        return this.userRepository.getReservationCount(user);
    }

    @Override
    public Integer getCommonProductsPrice(User user) {
        return this.userRepository.getCommonProductPrice(user);
    }

    @Override
    public Integer getSpendedMoneyOnReservations(User user) {
        return this.userRepository.getSpentMoneyOnReservations(user);
    }

    @Override
    public Integer getSoldedProductsCount(User user) {
        return this.userRepository.getSoldProductsCount(user);
    }

    @Override
    public List<ReservationInformation> getReservationInformation(User user) {

        ReservationToUser reservationToUser;

        try{
            reservationToUser =
             DataAccessUtils.objectResult(
                     Collections.singletonList(this.reservationToUserRepository.find(user.getId())),
                                               ReservationToUser.class
             );
        }catch(IncorrectResultSizeDataAccessException e){
            return null;
        }

        Set<Reservation> reservations = reservationToUser.getReservations();
        List<ReservationInformation> resultList = new ArrayList<>();
        AtomicInteger firstCounter = new AtomicInteger();
        firstCounter.set(1);
        Map<Product, AtomicInteger> productMap = new HashMap<>();

        reservations.forEach(reservation -> {

            ProductToReservation productToReservation =
                    this.productToReservationRepository.find(reservation.getId());
            List<Product> products = productToReservation.getProducts();
            productMap.clear();

            Integer price = products.stream()
                                    .mapToInt(Product::getPrice)
                                    .sum();

            IntStream.range(0, products.size()).forEachOrdered(i -> {

                Set<Product> productKeys = productMap.keySet();
                boolean exist = productKeys.contains(products.get(i));

                if (exist) {

                    AtomicInteger tempCounter = productMap.get(products.get(i));
                    tempCounter.set(tempCounter.get() + 1);
                    productMap.put(products.get(i), tempCounter);

                } else {
                    productMap.put(products.get(i), firstCounter);
                }

            });

            String prettyDate = this.simpleDateFormat.format(reservation.getCreatedAt());

            resultList.add(ReservationInformation.builder()
                                          .reservation(reservation)
                                          .prettyReservationDate(prettyDate)
                                          .price(price)
                                          .productWithCount(productMap)
                                          .build());

        });

        resultList.sort((ri1, ri2) -> {
            Reservation r1 = ri1.getReservation();
            Reservation r2 = ri2.getReservation();
            return r1.getId().compareTo(r2.getId());
        });

        return resultList;

    }

    @Override
    public List<AddressToUserTransfer> getAddressesByUser(User user) {

        AddressToUser addressToUser = this.addressToUserRepository.find(user.getId());
        EntityDTOInterface<AddressToUserTransfer, AddressToUser> entityDTO = new AddressToUserDTO();

        return entityDTO.convert(Collections.singletonList(addressToUser));

    }
}
