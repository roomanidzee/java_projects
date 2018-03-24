package ru.itis.romanov_andrey.perpenanto.services.interfaces.user;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;

import java.util.List;

/**
 * 02.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductService {

    List<Product> getProducts();
    List<Product> getProductsByCookie(String cookieValue);
    List<Product> getRandomProducts();

    void saveProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Product product);

}
