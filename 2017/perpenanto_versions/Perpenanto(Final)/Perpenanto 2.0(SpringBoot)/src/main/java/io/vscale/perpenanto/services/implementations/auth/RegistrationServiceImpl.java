package io.vscale.perpenanto.services.implementations.auth;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.vscale.perpenanto.forms.user.UserRegistrationForm;
import io.vscale.perpenanto.models.usermodels.Address;
import io.vscale.perpenanto.models.usermodels.AddressToUser;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.AddressRepository;
import io.vscale.perpenanto.repositories.interfaces.AddressToUserRepository;
import io.vscale.perpenanto.repositories.interfaces.ProfileRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.security.role.Role;
import io.vscale.perpenanto.security.states.UserState;
import io.vscale.perpenanto.services.interfaces.auth.RegistrationService;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationServiceImpl implements RegistrationService {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private AddressRepository addressRepository;
    private AddressToUserRepository addressToUserRepository;
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
