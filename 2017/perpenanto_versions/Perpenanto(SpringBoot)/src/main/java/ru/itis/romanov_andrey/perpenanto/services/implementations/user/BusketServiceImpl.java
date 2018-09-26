package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.repositories.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketServiceInterface;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 15.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class BusketServiceImpl implements BusketServiceInterface{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void addProductToBusket(Profile profile, Long productId) {

        Product product = this.productRepository.findOne(productId);

        if(profile.getProducts() == null){

            List<Product> products = new ArrayList<>();
            products.add(product);
            profile.setProducts(Collections.singletonList(product));

        }else{
            profile.getProducts().add(product);
        }

        this.profileRepository.save(profile);

    }

    @Override
    public void removeProductFromBusket(Profile profile, Long productId) {

        Product product = this.productRepository.findOne(productId);
        profile.getProducts().removeIf(product1 -> product1.equals(product));
        this.profileRepository.save(profile);

    }

    @Override
    public List<Product> getProductsFromBusket(Profile profile) {
        return profile.getProducts();
    }

    @Override
    public Integer getProductsCount(Profile profile) {
        return profile.getProducts().size();
    }

    @Override
    public void payForBusket(Profile profile, Reservation reservation) {

        List<Product> products = profile.getProducts();

        IntStream.range(0, products.size())
                 .forEachOrdered(i -> {

                        if (products.get(i).getReservations() != null) {
                            products.get(i).getReservations().add(reservation);
                        } else {
                            List<Reservation> reservations = new ArrayList<>();
                            reservations.add(reservation);
                            products.get(i).setReservations(reservations);
                        }

                 });

        profile.getProducts().addAll(products);
        profile.getUser().getReservations().add(reservation);

        IntStream.range(0, products.size())
                 .forEachOrdered(i -> profile.getProducts()
                                             .removeIf(product -> product.equals(products.get(i))));

        this.reservationRepository.save(reservation);
        this.profileRepository.save(profile);

    }

    @Override
    public Map<Reservation, Integer> getBusketPrice(Profile profile) {

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        Reservation reservation = Reservation.builder()
                                             .createdAt(Timestamp.valueOf(localDateTime))
                                             .status(ReservationState.COLLECTING)
                                             .build();

        this.reservationRepository.save(reservation);

        if(profile.getUser().getReservations() == null){

            Set<Reservation> reservations = new HashSet<>();
            reservations.add(reservation);
            profile.getUser().setReservations(reservations);

        }else{
            profile.getUser().getReservations().add(reservation);
        }

        int price = profile.getProducts()
                           .stream()
                           .mapToInt(Product::getPrice)
                           .sum();

        Map<Reservation, Integer> resultMap = new HashMap<>();
        resultMap.put(reservation, price);
        return resultMap;

    }
}
