package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductToUserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductToUserAdminServiceInterface;

import java.util.HashSet;
import java.util.Set;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToUserAdminServiceImpl implements ProductToUserAdminServiceInterface{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void workWithProductToUser(ProductToUserForm productToUserForm, String actionType) {

        User user = this.userRepository.findOne(productToUserForm.getUserId());
        Product product = this.productRepository.findOne(productToUserForm.getProductId());

        switch(actionType){

            case "add":

                if(user.getProducts() == null){

                    Set<Product> products = new HashSet<>();
                    products.add(product);
                    user.setProducts(products);

                }else{
                    user.getProducts().add(product);
                }

                break;

            case "update":

                user.getProducts().add(product);
                break;

            case "delete":

                user.getProducts().forEach(product1 -> {

                    if(product1.equals(product)){
                        user.getProducts().remove(product1);
                    }

                });

                break;
        }

    }

    @Override
    public void addProductToUser(ProductToUserForm productToUserForm) {

        User user = this.userRepository.findOne(productToUserForm.getUserId());
        Product product = this.productRepository.findOne(productToUserForm.getProductId());

        if(user.getProducts() == null){

            Set<Product> products = new HashSet<>();
            products.add(product);
            user.setProducts(products);

        }else{
            user.getProducts().add(product);
        }

    }

    @Override
    public void updateProductToUser(ProductToUserForm productToUserForm) {

        User user = this.userRepository.findOne(productToUserForm.getUserId());
        Product product = this.productRepository.findOne(productToUserForm.getProductId());

        user.getProducts().add(product);

    }

    @Override
    public void deleteProductToUser(Long userId, Long productId) {

        User user = this.userRepository.findOne(userId);
        Product product = this.productRepository.findOne(productId);

        user.getProducts().forEach(product1 -> {

            if(product1.equals(product)){
                user.getProducts().remove(product1);
            }

        });

    }
}
