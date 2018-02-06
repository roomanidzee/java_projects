package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductAdminService;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductAdminServiceImpl implements ProductAdminService{

    @Autowired
    private ProductRepository productRepository;

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
