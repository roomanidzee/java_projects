package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.forms.user.BusketInformation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;

import java.util.List;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface BusketService {

    void addProductToBusket(Profile profile, Long productId);
    void removeProductFromBusket(Profile profile, Long productId);
    List<Product> getProductsFromBusket(Profile profile);
    Integer getProductsCount(Profile profile);
    void payForBusket(Profile profile, Reservation reservation);
    BusketInformation getBusketPrice(Profile profile);

}
