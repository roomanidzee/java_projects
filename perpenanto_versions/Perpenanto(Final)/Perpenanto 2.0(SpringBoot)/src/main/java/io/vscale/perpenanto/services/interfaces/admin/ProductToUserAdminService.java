package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.forms.admin.ProductToUserForm;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToUserAdminService {

    void addProductToUser(ProductToUserForm productToUserForm);
    void updateProductToUser(ProductToUserForm productToUserForm);
    void deleteProductToUser(Long userId, Long productId);

}
