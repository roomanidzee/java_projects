package io.vscale.perpenanto.services.implementations.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.dto.implementations.AddressToUserDTO;
import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;
import io.vscale.perpenanto.utils.userutils.ReservationInformation;
import io.vscale.perpenanto.models.transfermodels.AddressToUserTransfer;
import io.vscale.perpenanto.models.usermodels.*;
import io.vscale.perpenanto.repositories.interfaces.*;
import io.vscale.perpenanto.services.interfaces.user.ProfileService;
import io.vscale.perpenanto.utils.userutils.CompareAttributes;

import java.text.SimpleDateFormat;
import java.util.*;
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
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProfileServiceImpl implements ProfileService{

    private ProfileRepository profileRepository;
    private UserRepository userRepository;
    private ProductToUserRepository productToUserRepository;
    private AddressToUserRepository addressToUserRepository;
    private ProductToReservationRepository productToReservationRepository;
    private ReservationToUserRepository reservationToUserRepository;
    private ReservationRepository reservationRepository;
    private JdbcTemplateWrapper<ProductToUser> productToUserJdbcTemplateWrapper;
    private JdbcTemplateWrapper<ReservationToUser> reservationToUserJdbcTemplateWrapper;

    @Override
    public List<Profile> getProfiles() {
        return this.profileRepository.findAll();
    }

    private List<Profile> sortList(List<Profile> oldList, Map<String, Function<Profile, String>> functionMap,
                                   String sortType) {

        List<Profile> resultList = new ArrayList<>();

        switch (sortType) {

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
    }

    @Override
    public List<Profile> getProfilesByCookie(String cookieValue) {

        CompareAttributes<Profile> compareAttributes = this::sortList;

        List<Profile> currentProfiles = this.profileRepository.findAll();

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

        return new ArrayList<>(compareAttributes.sortList(currentProfiles, functionMap, cookieValue));

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

        Optional<ProductToUser> tempProductToUser =
                this.productToUserJdbcTemplateWrapper.findItem(this.productToUserRepository, user.getId());

        if(!tempProductToUser.isPresent()){
            return null;
        }

        return tempProductToUser.get().getProducts();
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

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy (HH:mm:ss)");

        List<ReservationInformation> resultList = new ArrayList<>();

        Optional<ReservationToUser> tempReservationToUser =
                this.reservationToUserJdbcTemplateWrapper.findItem(this.reservationToUserRepository, user.getId());

        tempReservationToUser.ifPresent(reservationToUser -> {

            Set<Reservation> reservations = reservationToUser.getReservations();
            Map<Product, Long> productCount = new HashMap<>();

            reservations.forEach(reservation -> {

                productCount.clear();

                ProductToReservation productToReservation = this.productToReservationRepository.find(reservation.getId());

                List<Product> products = new ArrayList<>(productToReservation.getProducts());

                IntStream.range(0, products.size()).forEach(i ->
                        products.stream()
                                .filter(product -> product.equals(products.get(i)))
                                .forEach(product ->
                                        productCount.put(product,
                                                productCount.containsKey(product) ? productCount.get(product) + 1 : 1L)));


                Integer price = this.reservationRepository.getReservationPrice(reservation);

                String prettyDate = simpleDateFormat.format(reservation.getCreatedAt());

                resultList.add(ReservationInformation.builder()
                                                     .reservation(reservation)
                                                     .prettyReservationDate(prettyDate)
                                                     .price(price)
                                                     .products(products)
                                                     .productsCount(productCount)
                                                     .build());

            });

        });

        if(!resultList.isEmpty()){

            resultList.sort((ri1, ri2) -> {
                Reservation r1 = ri1.getReservation();
                Reservation r2 = ri2.getReservation();
                return r2.getId().compareTo(r1.getId());
            });

        }

        return resultList;

    }

    @Override
    public List<AddressToUserTransfer> getAddressesByUser(User user) {

        AddressToUser addressToUser = this.addressToUserRepository.find(user.getId());
        EntityDTOInterface<AddressToUserTransfer, AddressToUser> entityDTO = new AddressToUserDTO();

        return entityDTO.convert(Collections.singletonList(addressToUser));

    }


}
