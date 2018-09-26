package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;

import java.util.List;
import java.util.Map;

/**
 * 15.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface BusketServiceInterface {

    void addProductToBusket(Profile profile, Long productId);
    void removeProductFromBusket(Profile profile, Long productId);
    List<Product> getProductsFromBusket(Profile profile);
    Integer getProductsCount(Profile profile);
    void payForBusket(Profile profile, Reservation reservation);
    Map<Reservation, Integer> getBusketPrice(Profile profile);

}
