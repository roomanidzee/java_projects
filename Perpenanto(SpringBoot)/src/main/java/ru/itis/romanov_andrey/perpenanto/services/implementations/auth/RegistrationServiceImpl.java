package ru.itis.romanov_andrey.perpenanto.services.implementations.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.AddressRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.RegistrationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.SMSServiceInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class RegistrationServiceImpl implements RegistrationServiceInterface{

    @Autowired
    private SMSServiceInterface smsService;

    @Autowired
    private AdminServiceInterface adminService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegistrationForm userForm) {

        User newUser = User.builder()
                           .login(userForm.getLogin())
                           .protectedPassword(this.passwordEncoder.encode(userForm.getPassword()))
                           .role(Role.USER)
                           .state(UserState.NOT_CONFIRMED)
                           .build();

        Profile profile = Profile.builder()
                                 .personName(userForm.getPersonName())
                                 .personSurname(userForm.getPersonSurname())
                                 .phone(userForm.getPhone())
                                 .email(userForm.getEmail())
                                 .build();

        Address address = Address.builder()
                                 .country(userForm.getCountry())
                                 .postalCode(userForm.getPostalCode())
                                 .city(userForm.getCity())
                                 .street(userForm.getStreet())
                                 .homeNumber(userForm.getHomeNumber())
                                 .build();

        this.userRepository.save(newUser);
        profile.setUser(newUser);
        this.profileRepository.save(profile);
        List<User> users = new ArrayList<>();
        users.add(newUser);
        address.setUsers(users);
        this.addressRepository.save(address);

    }
}
