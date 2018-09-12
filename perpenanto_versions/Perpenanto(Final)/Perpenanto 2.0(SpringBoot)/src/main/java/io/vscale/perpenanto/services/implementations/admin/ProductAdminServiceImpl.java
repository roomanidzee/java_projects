package io.vscale.perpenanto.services.implementations.admin;

import io.vscale.perpenanto.forms.admin.ProductForm;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.services.interfaces.admin.ProductAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductAdminServiceImpl implements ProductAdminService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductAdminServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(ProductForm productForm) {

        Product product = Product.builder()
                                 .title(productForm.getTitle())
                                 .price(productForm.getPrice())
                                 .description(productForm.getDescription())
                                 .photoLink(productForm.getPhotolink())
                                 .build();
        this.productRepository.save(product);

    }

    @Override
    public void updateProduct(ProductForm productForm) {

        Product product = Product.builder()
                                 .id(productForm.getId())
                                 .title(productForm.getTitle())
                                 .price(productForm.getPrice())
                                 .description(productForm.getDescription())
                                 .photoLink(productForm.getPhotolink())
                                 .build();
        this.productRepository.update(product);

    }

    @Override
    public void deleteProduct(Long id) {
        this.productRepository.delete(id);
    }
}
