package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductAdminService {

    void addProduct(ProductForm productForm);
    void updateProduct(ProductForm productForm);
    void deleteProduct(Long id);

}
