package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductToUserForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductToUserRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductToUserAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToUserAdminServiceImpl implements ProductToUserAdminService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductToUserRepository productToUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addProductToUser(ProductToUserForm productToUserForm) {

        ProductToUser productToUser = ProductToUser.builder()
                                                   .user(this.userRepository.find(productToUserForm.getUserId()))
                                                   .products(Sets.newHashSet(
                                                           this.productRepository.find(productToUserForm.getProductId())
                                                   ))
                                                   .build();
        this.productToUserRepository.save(productToUser);

    }

    @Override
    public void updateProductToUser(ProductToUserForm productToUserForm) {

        ProductToUser productToUser = ProductToUser.builder()
                                                   .user(this.userRepository.find(productToUserForm.getUserId()))
                                                   .products(Sets.newHashSet(
                                                           this.productRepository.find(productToUserForm.getProductId())
                                                   ))
                                                   .build();
        this.productToUserRepository.update(productToUser);

    }

    @Override
    public void deleteProductToUser(Long userId, Long productId) {
        this.productToUserRepository.delete(userId, productId);
    }
}
