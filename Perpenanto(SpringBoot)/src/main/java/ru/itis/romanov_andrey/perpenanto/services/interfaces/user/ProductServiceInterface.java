package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ProductToUser;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductServiceInterface {

    List<Product> getProducts();
    List<Product> getProductsByCookie(String cookieValue);
    List<ProductToUser> getProductsToUser();
    List<ProductToUser> getProductsToUserByCookie(String cookieValue);
    void saveOrUpdate(Product product);
    void delete(Long id);

}
