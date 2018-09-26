package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.forms.user.ProductCreateForm;
import io.vscale.perpenanto.models.transfermodels.AddressToUserTransfer;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.security.role.Role;

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
