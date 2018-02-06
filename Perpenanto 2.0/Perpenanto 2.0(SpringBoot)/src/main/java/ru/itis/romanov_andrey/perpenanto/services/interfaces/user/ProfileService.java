package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.forms.user.ReservationInformation;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

import java.util.List;
import java.util.Set;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileService {

    List<Profile> getProfiles();
    List<Profile> getProfilesByCookie(String cookieValue);

    void saveProfile(Profile profile);
    void updateProfile(Profile profile);
    void deleteProfile(Profile profile);

    Profile findByUserId(Long userId);

    Set<Product> getProductsByUser(User user);
    Integer countReservations(User user);
    Integer getCommonProductsPrice(User user);
    Integer getSpendedMoneyOnReservations(User user);
    Integer getSoldedProductsCount(User user);
    List<ReservationInformation> getReservationInformation(User user);
    List<AddressToUserTransfer> getAddressesByUser(User user);

}
