package io.vscale.perpenanto.services.implementations.user;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.user.ProductCreateForm;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.ProductToUser;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.repositories.interfaces.ProductToUserRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.security.role.Role;
import io.vscale.perpenanto.services.interfaces.user.UserService;
import io.vscale.perpenanto.utils.userutils.CompareAttributes;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Optional;
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

    @Autowired
    private JdbcTemplateWrapper<ProductToUser> jdbcTemplateWrapper;

    private List<User> sortList(List<User> oldList, Map<String, Function<User, String>> functionMap, String sortType) {

        List<User> resultList = new ArrayList<>();

        switch (sortType) {

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
    }

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

        CompareAttributes<User> compareAttributes = this::sortList;

        List<User> currentUsers = getUsersByRole(role);

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

        return new ArrayList<>(compareAttributes.sortList(currentUsers, functionMap, cookieValue));

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

        Optional<ProductToUser> tempProductToUser =
                this.jdbcTemplateWrapper.findItem(this.productToUserRepository, user.getId());

        tempProductToUser.ifPresent(productToUser -> {
            productToUser.getProducts().add(product);
            this.productToUserRepository.update(productToUser);
        });

        if(!tempProductToUser.isPresent()){

            ProductToUser productToUser = ProductToUser.builder()
                                                       .user(user)
                                                       .products(Sets.newHashSet(product))
                                                       .build();

            this.productToUserRepository.save(productToUser);

        }

    }
}
