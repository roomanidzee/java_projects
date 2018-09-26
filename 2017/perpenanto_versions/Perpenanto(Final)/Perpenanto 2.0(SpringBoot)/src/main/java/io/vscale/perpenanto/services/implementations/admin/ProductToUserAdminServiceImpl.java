package io.vscale.perpenanto.services.implementations.admin;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.admin.ProductToUserForm;
import io.vscale.perpenanto.models.usermodels.ProductToUser;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.repositories.interfaces.ProductToUserRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.services.interfaces.admin.ProductToUserAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToUserAdminServiceImpl implements ProductToUserAdminService{

    private final ProductRepository productRepository;

    private final ProductToUserRepository productToUserRepository;

    private final UserRepository userRepository;

    @Autowired
    public ProductToUserAdminServiceImpl(ProductRepository productRepository,
                                         ProductToUserRepository productToUserRepository,
                                         UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productToUserRepository = productToUserRepository;
        this.userRepository = userRepository;
    }

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
