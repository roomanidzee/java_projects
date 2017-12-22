package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.util.List;
import java.util.Set;
import java.util.Map;

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

    Profile findByUserId(Long userId);

    int countReservations(User user);
    Set<Product> getProductsByUser(User user);
    int getCommonProductsPrice(User user);
    int getSpendedMoneyOnReservations(User user);
    int getSoldedProductsCount(User user);
    Map<Reservation, Integer> getReservationInformation(User user);

}
