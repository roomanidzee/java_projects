package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.models.transfermodels.ProductToUserTransfer;
import io.vscale.perpenanto.models.usermodels.ProductToUser;

import java.util.List;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToUserService {

    List<ProductToUserTransfer> getProductsToUsers();
    List<ProductToUserTransfer> getProductsToUsersByCookie(String cookieValue);

    void saveProductToUser(ProductToUser productToUser);
    void updateProductToUser(ProductToUser productToUser);
    void deleteProductToUser(Long userId, Long productId);

}
