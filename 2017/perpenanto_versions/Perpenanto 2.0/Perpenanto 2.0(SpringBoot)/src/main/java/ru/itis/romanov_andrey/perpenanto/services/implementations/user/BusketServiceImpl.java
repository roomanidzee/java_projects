package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.user.BusketInformation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Busket;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.BusketRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.BusketService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class BusketServiceImpl implements BusketService{

    @Autowired
    private BusketRepository busketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProductToBusket(Profile profile, Long productId) {

        Product product = this.productRepository.find(productId);
        Busket busket;

        try{

            busket =
                    DataAccessUtils.objectResult(Collections.singletonList(this.busketRepository.find(profile.getId())),
                                                 Busket.class);

            busket.getProducts().add(product);
            this.busketRepository.update(busket);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){

            busket = Busket.builder()
                           .profile(profile)
                           .products(Lists.newArrayList(product))
                           .build();
            this.busketRepository.update(busket);

        }

    }

    @Override
    public void removeProductFromBusket(Profile profile, Long productId) {
        this.busketRepository.delete(profile.getId(), productId);
    }

    @Override
    public List<Product> getProductsFromBusket(Profile profile) {
        Busket busket = this.busketRepository.find(profile.getId());
        return busket.getProducts();
    }

    @Override
    public Integer getProductsCount(Profile profile) {

        Busket busket;

        try{
            busket =
                    DataAccessUtils.objectResult(Collections.singletonList(this.busketRepository.find(profile.getId())),
                            Busket.class);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){
            return null;
        }

        return busket.getProducts().size();
    }

    @Override
    public void payForBusket(Profile profile, Reservation reservation) {

        reservation.setReservationState(ReservationState.COLLECTING);
        this.reservationRepository.save(reservation);
        Busket busket = this.busketRepository.find(profile.getId());
        this.busketRepository.payForBusket(busket, reservation);

    }

    @Override
    public BusketInformation getBusketPrice(Profile profile) {

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        Reservation reservation = Reservation.builder()
                                             .createdAt(Timestamp.valueOf(localDateTime))
                                             .build();

        Busket busket = this.busketRepository.find(profile.getId());
        List<Product> products = busket.getProducts();

        int price = products.stream()
                            .mapToInt(Product::getPrice)
                            .sum();

        return BusketInformation.builder()
                                .reservation(reservation)
                                .price(price)
                                .build();

    }
}
