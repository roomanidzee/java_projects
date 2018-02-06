package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ProductToReservationTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToReservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToReservationService {

    List<ProductToReservationTransfer> getProductsToReservations();
    List<ProductToReservationTransfer> getProductsToReservationsByCookie(String cookieValue);

    void saveProductToReservation(ProductToReservation model);
    void updateProductToReservation(ProductToReservation model);
    void deleteProductToReservation(ProductToReservation model);

    List<Product> getReservationProducts(User user, Timestamp timestamp);
    Integer getReservationPrice(Timestamp timestamp);

}
