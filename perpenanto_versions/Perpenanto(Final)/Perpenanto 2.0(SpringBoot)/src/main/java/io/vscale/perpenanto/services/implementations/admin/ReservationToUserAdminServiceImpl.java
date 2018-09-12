package io.vscale.perpenanto.services.implementations.admin;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.admin.ReservationToUserForm;
import io.vscale.perpenanto.models.usermodels.ReservationToUser;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.repositories.interfaces.ReservationToUserRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.services.interfaces.admin.ReservationToUserAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ReservationToUserAdminServiceImpl implements ReservationToUserAdminService{

    private final ReservationToUserRepository reservationToUserRepository;

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    @Autowired
    public ReservationToUserAdminServiceImpl(ReservationToUserRepository reservationToUserRepository,
                                             ReservationRepository reservationRepository,
                                             UserRepository userRepository) {
        this.reservationToUserRepository = reservationToUserRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

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
