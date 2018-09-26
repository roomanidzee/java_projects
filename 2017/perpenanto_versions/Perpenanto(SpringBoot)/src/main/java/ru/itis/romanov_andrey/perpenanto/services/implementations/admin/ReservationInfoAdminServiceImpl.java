package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationInfoForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationInfoAdminServiceInterface;

import java.util.List;

/**
 * 28.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationInfoAdminServiceImpl implements ReservationInfoAdminServiceInterface{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addReservationInfo(ReservationInfoForm reservationInfoForm) {

        User user = this.userRepository.findOne(reservationInfoForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(reservationInfoForm.getOrderId());
        Product product = this.productRepository.findOne(reservationInfoForm.getProductId());

        Reservation changeableReservation = user.getReservations()
                                                .stream()
                                                .filter(reservation1 -> reservation1.equals(reservation))
                                                .findFirst()
                                                .orElseThrow(() -> new NullPointerException("No such Reservation!"));

        changeableReservation.getProducts().add(product);
        this.reservationRepository.save(changeableReservation);

    }

    @Override
    public void updateReservationInfo(ReservationInfoForm reservationInfoForm) {

        User user = this.userRepository.findOne(reservationInfoForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(reservationInfoForm.getOrderId());
        Product product = this.productRepository.findOne(reservationInfoForm.getProductId());

        user.getReservations()
                .forEach(reservation1 -> {
                    if(reservation1.equals(reservation)){
                        reservation1.getProducts().add(product);
                    }
                });

        this.userRepository.save(user);

    }

    @Override
    public void deleteReservationInfo(Long productId) {

        Product product = this.productRepository.findOne(productId);

        List<User> users = this.userRepository.findAll();

        users.stream()
                .map(User::getReservations)
                .forEachOrdered(reservations ->
                        reservations.forEach(
                                reservation1 -> {

                                    List<Product> products = reservation1.getProducts();

                                    products.forEach(product1 -> {
                                        if (product1.equals(product)) {
                                            reservation1.getProducts().remove(product);
                                        }
                                    });

                                }
                        )
                );

        this.userRepository.save(users);

    }
}
