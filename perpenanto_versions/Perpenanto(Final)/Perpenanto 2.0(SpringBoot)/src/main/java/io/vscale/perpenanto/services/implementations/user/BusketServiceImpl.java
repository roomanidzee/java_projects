package io.vscale.perpenanto.services.implementations.user;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.utils.userutils.BusketInformation;
import io.vscale.perpenanto.models.usermodels.Busket;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.repositories.interfaces.BusketRepository;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.security.states.ReservationState;
import io.vscale.perpenanto.services.interfaces.user.BusketService;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusketServiceImpl implements BusketService{

    private BusketRepository busketRepository;
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private JdbcTemplateWrapper<Busket> jdbcTemplateWrapper;

    @Override
    public void addProductToBusket(Profile profile, Long productId) {

        Product product = this.productRepository.find(productId);
        Optional<Busket> tempBusket =  this.jdbcTemplateWrapper.findItem(this.busketRepository, profile.getId());

        if(!tempBusket.isPresent()){

            Busket busket = Busket.builder()
                                  .profile(profile)
                                  .products(Lists.newArrayList(product))
                                  .build();

            this.busketRepository.update(busket);
        }

        tempBusket.ifPresent(busket -> {
            busket.getProducts().add(product);
            this.busketRepository.update(busket);
        });

    }

    @Override
    public void removeProductFromBusket(Profile profile, Long productId) {
        this.busketRepository.delete(profile.getId(), productId);
    }

    @Override
    public Map<Product, Long> getProductsFromBusket(Profile profile) {

        Optional<Busket> tempBusket =  this.jdbcTemplateWrapper.findItem(this.busketRepository, profile.getId());

        if(!tempBusket.isPresent()){
            throw new NullPointerException("В корзине нет товаров!");
        }

        List<Product> products = tempBusket.get().getProducts();

        return products.stream()
                       .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    }

    @Override
    public Integer getProductsCount(Profile profile) {

        Optional<Busket> tempBusket =  this.jdbcTemplateWrapper.findItem(this.busketRepository, profile.getId());
        return tempBusket.map(busket -> busket.getProducts()
                                              .size()
                          )
                          .orElse(null);

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


        Optional<Busket> tempBusket =  this.jdbcTemplateWrapper.findItem(this.busketRepository, profile.getId());

        if(!tempBusket.isPresent()){
            return null;
        }

        List<Product> products = tempBusket.get().getProducts();

        int price = products.stream()
                            .mapToInt(Product::getPrice)
                            .sum();

        return BusketInformation.builder()
                                .reservation(reservation)
                                .price(price)
                                .build();

    }
}
