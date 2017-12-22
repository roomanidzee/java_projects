package ru.itis.romanov_andrey.perpenanto.services.interfaces.admin;

import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductToUserForm;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToUserAdminServiceInterface {

    void workWithProductToUser(ProductToUserForm productToUserForm, String actionType);
    void addProductToUser(ProductToUserForm productToUserForm);
    void updateProductToUser(ProductToUserForm productToUserForm);
    void deleteProductToUser(Long userId, Long productId);

}
