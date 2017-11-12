package ru.itis.romanov_andrey.perpenanto.services.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.Product;
import ru.itis.romanov_andrey.perpenanto.models.Profile;
import ru.itis.romanov_andrey.perpenanto.models.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileServiceInterface {

    List<Profile> getProfiles();
    List<Profile> getProfilesByCookie(String cookieValue);
    void saveOrUpdate(Profile profile);
    void delete(Long id);

    int countReservations(User user);
    Set<Product> getProductsByUser(User user);
    int getCommonProductsPrice(User user);
    int getSpendedMoneyOnReservations(User user);
    int getSoldedProductsCount(User user);
    Map<Reservation, Integer> getReservationInformation(User user);

}
