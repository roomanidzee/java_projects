package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.UserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.UserAdminService;

import java.util.Arrays;
import java.util.Optional;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserAdminServiceImpl implements UserAdminService{

    @Autowired
    private UserRepository userRepository;

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
