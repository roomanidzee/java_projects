package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.user.AddProductForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.UserServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;
import ru.itis.romanov_andrey.perpenanto.utils.StreamCompareAttributes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserServiceInterface{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveOrUpdate(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    @Override
    public Optional<User>  findByConfirmHash(String confirmHash) {
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
    public void addProduct(User user, AddProductForm addProductForm) {

        Product product = Product.builder()
                                 .title(addProductForm.getTitle())
                                 .price(addProductForm.getPrice())
                                 .description(addProductForm.getDescription())
                                 .photoLink(addProductForm.getPhotolink())
                                 .build();

        this.productRepository.save(product);

        if(user.getProducts() == null){

            Set<Product> products = new HashSet<>();
            products.add(product);
            user.setProducts(products);

        }else{
            user.getProducts().add(product);
        }

        this.userRepository.save(user);

    }

    @Override
    public List<User> getUsers(){
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

        Function<User, String> zero = (User u) -> String.valueOf(u.getId());
        Function<User, String> first = User::getLogin;
        Function<User, String> second = User::getProtectedPassword;

        List<Function<User, String>> functions = Arrays.asList(zero, first, second);
        List<String> indexes = Arrays.asList("0", "1", "2");

        Map<String, Function<User, String>> functionMap = new HashMap<>();
        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<User> compareAttr = new StreamCompareAttributes<>();
        sortedUsers.addAll(compareAttr.sortList(currentUsers, functionMap, cookieValue));

        return sortedUsers;

    }

}
