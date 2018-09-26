package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.utils.userutils.BusketInformation;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.Reservation;

import java.util.Map;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface BusketService {

    void addProductToBusket(Profile profile, Long productId);
    void removeProductFromBusket(Profile profile, Long productId);
    Map<Product, Long> getProductsFromBusket(Profile profile);
    Integer getProductsCount(Profile profile);
    void payForBusket(Profile profile, Reservation reservation);
    BusketInformation getBusketPrice(Profile profile);

}
