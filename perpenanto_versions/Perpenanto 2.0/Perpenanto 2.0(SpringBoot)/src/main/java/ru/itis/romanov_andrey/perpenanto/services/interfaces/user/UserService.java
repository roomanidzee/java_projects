package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.forms.user.ProductCreateForm;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;

import java.util.List;
import java.util.Optional;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface UserService {

    List<User> getUsers();
    List<User> getUsersByRole(Role role);
    List<User> getUsersByRoleAndCookie(Role role, String cookieValue);

    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(User user);

    Optional<User> findByConfirmHash(String confirmHash);
    Optional<User> findByLogin(String login);
    Optional<User> findById(Long id);

    void addProduct(User user, ProductCreateForm productCreateForm);


}
