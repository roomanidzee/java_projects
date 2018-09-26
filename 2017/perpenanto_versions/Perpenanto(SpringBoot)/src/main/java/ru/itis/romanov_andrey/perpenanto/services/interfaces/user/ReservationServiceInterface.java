package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationInfo;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ReservationToUser;

import java.sql.Timestamp;
import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ReservationServiceInterface {

    List<Reservation> getReservations();
    List<Reservation> getReservationsByCookie(String cookieValue);
    List<ReservationInfo> getReservationInfosByCookie(String cookieValue);
    List<ReservationToUser> getReservationToUsersByCookie(String cookieValue);
    void saveOrUpdate(Reservation reservation);
    void delete(Long id);

    Long getReservationId(Timestamp timestamp);
    List<Product> getReservationProducts(Profile profile, Timestamp timestamp);
    Integer getReservationPrice(Timestamp timestamp);
    Reservation getReservationByTimestamp(Timestamp timestamp);

}
