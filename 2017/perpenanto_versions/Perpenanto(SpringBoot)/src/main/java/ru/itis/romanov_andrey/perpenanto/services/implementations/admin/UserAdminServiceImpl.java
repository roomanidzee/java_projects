package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.UserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.UserAdminServiceInterface;

import java.util.Arrays;

/**
 * 28.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserAdminServiceImpl implements UserAdminServiceInterface{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserState[] userStatesTemp = UserState.values();
    private Role[] rolesTemp = Role.values();

    @Override
    public void workWithUser(UserForm userForm, String actionType) {

        UserState[] userStatesTemp = UserState.values();
        Role[] rolesTemp = Role.values();

        switch(actionType){

            case "add":

                UserState userState = Arrays.stream(userStatesTemp)
                                  .filter(userState1 -> userForm.getState()
                                          .toUpperCase()
                                          .equals(userState1.toString()))
                                  .findFirst()
                                  .orElseThrow(() -> new NullPointerException("No such User state!"));

                Role role = Arrays.stream(rolesTemp)
                                  .filter(role1 -> userForm.getRole()
                                          .toUpperCase()
                                          .equals(role1.toString()))
                                  .findFirst()
                                  .orElseThrow(() -> new NullPointerException("No such User state!"));

                User user = User.builder()
                                .login(userForm.getLogin())
                                .protectedPassword(this.passwordEncoder.encode(userForm.getPassword()))
                                .role(role)
                                .state(userState)
                                .build();
                this.userRepository.save(user);
                break;

            case "update":

                UserState userState3 = Arrays.stream(userStatesTemp)
                                             .filter(userState4 -> userForm.getState()
                                             .toUpperCase()
                                             .equals(userState4.toString()))
                                             .findFirst()
                                             .orElseThrow(() -> new NullPointerException("No such User state!"));

                Role role1 = Arrays.stream(rolesTemp)
                                   .filter(role2 -> userForm.getRole()
                                   .toUpperCase()
                                   .equals(role2.toString()))
                                   .findFirst()
                                   .orElseThrow(() -> new NullPointerException("No such User state!"));

                User user1 = this.userRepository.findOne(userForm.getId());

                user1.setLogin(userForm.getLogin());
                user1.setProtectedPassword(this.passwordEncoder.encode(userForm.getPassword()));
                user1.setRole(role1);
                user1.setState(userState3);
                this.userRepository.save(user1);
                break;

            case "delete":

                this.userRepository.delete(userForm.getId());
                break;

        }

    }

    @Override
    public void addUser(UserForm userForm) {

        UserState userState = Arrays.stream(this.userStatesTemp)
                                    .filter(userState1 -> userForm.getState()
                                                                  .toUpperCase()
                                                                  .equals(userState1.toString()))
                                    .findFirst()
                                    .orElseThrow(() -> new NullPointerException("No such User state!"));

        Role role = Arrays.stream(this.rolesTemp)
                          .filter(role1 -> userForm.getRole()
                                                   .toUpperCase()
                                                   .equals(role1.toString()))
                          .findFirst()
                          .orElseThrow(() -> new NullPointerException("No such User state!"));

        User user = User.builder()
                        .login(userForm.getLogin())
                        .protectedPassword(this.passwordEncoder.encode(userForm.getPassword()))
                        .role(role)
                        .state(userState)
                        .build();

        this.userRepository.save(user);

    }

    @Override
    public void updateUser(UserForm userForm) {

        UserState userState = Arrays.stream(this.userStatesTemp)
                                    .filter(userState1 -> userForm.getState()
                                                                  .toUpperCase()
                                                                  .equals(userState1.toString()))
                                    .findFirst()
                                    .orElseThrow(() -> new NullPointerException("No such User state!"));

        Role role = Arrays.stream(this.rolesTemp)
                          .filter(role1 -> userForm.getRole()
                                                   .toUpperCase()
                                                   .equals(role1.toString()))
                          .findFirst()
                          .orElseThrow(() -> new NullPointerException("No such User state!"));

        User user1 = this.userRepository.findOne(userForm.getId());

        user1.setLogin(userForm.getLogin());
        user1.setProtectedPassword(this.passwordEncoder.encode(userForm.getPassword()));
        user1.setRole(role);
        user1.setState(userState);
        this.userRepository.save(user1);

    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.delete(id);
    }
}
