package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.forms.user.AddProductForm;
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
public interface UserServiceInterface {

    List<User> getUsers();
    List<User> getUsersByRole(Role role);
    List<User> getUsersByRoleAndCookie(Role role, String cookieValue);
    void saveOrUpdate(User user);
    void delete(Long id);
    Optional<User> findByConfirmHash(String confirmHash);
    Optional<User> findByLogin(String login);
    Optional<User> findById(Long id);
    void addProduct(User user, AddProductForm addProductForm);

}
