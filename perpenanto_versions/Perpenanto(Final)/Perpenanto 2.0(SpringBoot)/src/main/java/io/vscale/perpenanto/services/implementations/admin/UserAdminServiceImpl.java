package io.vscale.perpenanto.services.implementations.admin;

import io.vscale.perpenanto.forms.admin.UserForm;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.security.role.Role;
import io.vscale.perpenanto.security.states.UserState;
import io.vscale.perpenanto.services.interfaces.admin.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;

    @Autowired
    public UserAdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(UserForm userForm) {

        Role[] roles = Role.values();
        UserState[] userStates = UserState.values();

        Optional<Role> role = Arrays.stream(roles)
                                    .filter(role1 -> role1.toString().equals(userForm.getRole()))
                                    .findFirst();
        Optional<UserState> userState = Arrays.stream(userStates)
                                              .filter(userState1 -> userState1.toString().equals(userForm.getState()))
                                              .findFirst();

        if(!role.isPresent()){
            throw new NullPointerException("role not found!");
        }

        if(!userState.isPresent()){
            throw new NullPointerException("user state not found!");
        }

        User user = User.builder()
                        .login(userForm.getLogin())
                        .password(userForm.getPassword())
                        .role(role.get())
                        .userState(userState.get())
                        .build();
        this.userRepository.save(user);

    }

    @Override
    public void updateUser(UserForm userForm) {

        Role[] roles = Role.values();
        UserState[] userStates = UserState.values();

        Optional<Role> role = Arrays.stream(roles)
                                    .filter(role1 -> role1.toString().equals(userForm.getRole()))
                                    .findFirst();
        Optional<UserState> userState = Arrays.stream(userStates)
                                              .filter(userState1 -> userState1.toString().equals(userForm.getState()))
                                              .findFirst();

        if(!role.isPresent()){
            throw new NullPointerException("role not found!");
        }

        if(!userState.isPresent()){
            throw new NullPointerException("user state not found!");
        }

        User user = User.builder()
                        .id(userForm.getId())
                        .login(userForm.getLogin())
                        .password(userForm.getPassword())
                        .role(role.get())
                        .userState(userState.get())
                        .build();
        this.userRepository.update(user);

    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.delete(id);
    }
}
