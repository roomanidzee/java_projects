package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.utils.userutils.ReservationInformation;
import io.vscale.perpenanto.models.transfermodels.AddressToUserTransfer;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;

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
