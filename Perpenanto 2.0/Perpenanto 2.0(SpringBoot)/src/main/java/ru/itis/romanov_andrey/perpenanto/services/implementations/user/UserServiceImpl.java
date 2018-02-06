package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.AddressToUserDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.forms.user.ProductCreateForm;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductToUserRepository productToUserRepository;

    @Autowired
    private ProductRepository productRepository;

    private CompareAttributes<User> compareAttributes = (oldList, functionMap, sortType) -> {

        List<User> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                resultList.addAll(this.userRepository.findAllByRole(Role.USER));
                break;

            case "id":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("id")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "login":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("login")))
                                    .collect(Collectors.toList());
                break;

            case "password":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("password")))
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return this.userRepository.findAllByRole(role);
    }

    @Override
    public List<User> getUsersByRoleAndCookie(Role role, String cookieValue) {

        List<User> currentUsers = getUsersByRole(role);
        List<User> sortedUsers = new ArrayList<>();

        int size = 3;

        Function<User, String> idFunction = user -> String.valueOf(user.getId());
        Function<User, String> loginFunction = User::getLogin;
        Function<User, String> passwordFunction = User::getPassword;

        List<Function<User, String>> functions = Arrays.asList(idFunction, loginFunction, passwordFunction);
        List<String> sortTypes = Arrays.asList("id", "login", "password");

        Map<String, Function<User, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEach(i ->{

            String sortType = sortTypes.get(i);
            Function<User, String> function = functions.get(i);
            functionMap.put(sortType, function);

        });

        sortedUsers.addAll(this.compareAttributes.sortList(currentUsers, functionMap, cookieValue));
        return sortedUsers;

    }

    @Override
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        this.userRepository.update(user);
    }

    @Override
    public void deleteUser(User user) {
        this.userRepository.delete(user.getId());
    }

    @Override
    public Optional<User> findByConfirmHash(String confirmHash) {
        return this.userRepository.findByConfirmHash(confirmHash);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return this.userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public void addProduct(User user, ProductCreateForm productCreateForm) {

        if(productCreateForm.getPrice() < 0){
            throw new IllegalArgumentException("price must be great than 0!");
        }

        Product product = Product.builder()
                                 .title(productCreateForm.getTitle())
                                 .price(productCreateForm.getPrice())
                                 .description(productCreateForm.getDescription())
                                 .photoLink(productCreateForm.getPhotolink())
                                 .build();

        this.productRepository.save(product);

        ProductToUser productToUser = this.productToUserRepository.find(user.getId());

        if(productToUser == null){

            productToUser = ProductToUser.builder()
                                         .user(user)
                                         .products(Sets.newHashSet(product))
                                         .build();
            this.productToUserRepository.save(productToUser);

        }else{
            productToUser.getProducts().add(product);
            this.productToUserRepository.update(productToUser);
        }

    }
}
