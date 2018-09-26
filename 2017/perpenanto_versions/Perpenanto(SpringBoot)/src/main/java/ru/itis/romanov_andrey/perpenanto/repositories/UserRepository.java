package ru.itis.romanov_andrey.perpenanto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;

import java.util.List;
import java.util.Optional;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByLogin(String login);
    Optional<User> findById(Long userId);
    Optional<User> findByConfirmHash(String confirmHash);
    List<User> findAllByRole(Role role);

}
