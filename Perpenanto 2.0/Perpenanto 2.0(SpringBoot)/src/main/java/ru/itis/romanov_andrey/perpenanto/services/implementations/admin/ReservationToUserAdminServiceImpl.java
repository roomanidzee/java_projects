package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ReservationToUserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ReservationToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ReservationToUserAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationToUserAdminServiceImpl implements ReservationToUserAdminService{

    @Autowired
    private ReservationToUserRepository reservationToUserRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addReservationToUser(ReservationToUserForm reservationToUserForm) {

        ReservationToUser reservationToUser =
                ReservationToUser.builder()
                                 .user(this.userRepository.find(reservationToUserForm.getUserId()))
                                 .reservations(Sets.newHashSet(
                                         this.reservationRepository.find(reservationToUserForm.getOrderId())
                                 ))
                                 .build();
        this.reservationToUserRepository.save(reservationToUser);

    }

    @Override
    public void updateReservationToUser(ReservationToUserForm reservationToUserForm) {

        ReservationToUser reservationToUser =
                ReservationToUser.builder()
                                 .user(this.userRepository.find(reservationToUserForm.getUserId()))
                                 .reservations(Sets.newHashSet(
                                         this.reservationRepository.find(reservationToUserForm.getOrderId())
                                 ))
                                 .build();
        this.reservationToUserRepository.update(reservationToUser);

    }

    @Override
    public void deleteReservationToUser(Long userId, Long orderId) {
        this.reservationToUserRepository.delete(userId, orderId);
    }
}
