package ru.itis.romanov_andrey.perpenanto.services.implementations.auth;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.auth.RegistrationService;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressToUserRepository addressToUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(UserRegistrationForm userForm) {

        userForm.setPhone(userForm.getPhone().replaceAll("\\+", "")
                                             .replaceAll(" ", "")
                                             .replaceAll("\\+", ""));

        User newUser = User.builder()
                           .login(userForm.getLogin())
                           .password(this.passwordEncoder.encode(userForm.getPassword()))
                           .role(Role.USER)
                           .userState(UserState.NOT_CONFIRMED)
                           .build();

        this.userRepository.save(newUser);

        Profile profile = Profile.builder()
                                 .name(userForm.getPersonName())
                                 .surname(userForm.getPersonSurname())
                                 .user(newUser)
                                 .phone(userForm.getPhone())
                                 .email(userForm.getEmail())
                                 .build();

        this.profileRepository.save(profile);

        Address address = Address.builder()
                                 .country(userForm.getCountry())
                                 .postalCode(userForm.getPostalCode())
                                 .city(userForm.getCity())
                                 .street(userForm.getStreet())
                                 .homeNumber(userForm.getHomeNumber())
                                 .build();

        this.addressRepository.save(address);

        AddressToUser addressToUser = AddressToUser.builder()
                                                   .user(newUser)
                                                   .addresses(Sets.newHashSet(address))
                                                   .build();

        this.addressToUserRepository.save(addressToUser);

    }
}
