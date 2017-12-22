package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationToUserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationToUserAdminServiceInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 28.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationToUserAdminServiceImpl implements ReservationToUserAdminServiceInterface{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void workWithReservationToUser(ReservationToUserForm reservationToUserForm, String actionType) {

        User user = this.userRepository.findOne(reservationToUserForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(reservationToUserForm.getOrderId());

        switch(actionType){

            case "add":

                if(user.getReservations() == null){

                    Set<Reservation> reservations = new HashSet<>();
                    reservations.add(reservation);
                    user.setReservations(reservations);

                }else{
                    user.getReservations().add(reservation);
                }

                break;

            case "update":

                user.getReservations().add(reservation);
                break;

            case "delete":

                Set<Reservation> reservations = user.getReservations();

                reservations.forEach(reservation1 -> {
                    if(reservation1.equals(reservation)){
                        user.getReservations().remove(reservation);
                    }
                });
                break;

        }

    }

    @Override
    public void addReservationToUser(ReservationToUserForm reservationToUserForm) {

        User user = this.userRepository.findOne(reservationToUserForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(reservationToUserForm.getOrderId());

        if(user.getReservations() == null){

            Set<Reservation> reservations = new HashSet<>();
            reservations.add(reservation);
            user.setReservations(reservations);

        }else{
            user.getReservations().add(reservation);
        }

    }

    @Override
    public void updateReservationToUser(ReservationToUserForm reservationToUserForm) {

        User user = this.userRepository.findOne(reservationToUserForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(reservationToUserForm.getOrderId());
        user.getReservations().add(reservation);

    }

    @Override
    public void deleteReservationToUser(Long orderId) {

        Reservation reservation = this.reservationRepository.findOne(orderId);
        List<User> users = this.userRepository.findAll();

        IntStream.range(0, users.size()).forEachOrdered(i -> {

            Set<Reservation> reservations = users.get(i).getReservations();

            reservations.forEach(reservation1 -> {
                if (reservation1.equals(reservation)) {
                    users.get(i).getReservations().remove(reservation);
                }
            });

        });

    }
}
