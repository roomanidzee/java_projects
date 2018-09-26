package io.vscale.perpenanto.repositories.interfaces;

import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.security.role.Role;

import java.util.List;
import java.util.Optional;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface UserRepository extends CrudDAOInterface<User, Long>{

    Optional<User> findByLogin(String login);
    Optional<User> findById(Long userId);
    Optional<User> findByConfirmHash(String confirmHash);
    List<User> findAllByRole(Role role);

    Integer getSoldProductsCount(User user);
    Integer getSpentMoneyOnReservations(User user);
    Integer getReservationCount(User user);
    Integer getCommonProductPrice(User user);

}
