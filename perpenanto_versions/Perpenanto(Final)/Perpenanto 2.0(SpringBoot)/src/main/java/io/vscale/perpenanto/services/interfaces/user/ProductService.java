package io.vscale.perpenanto.services.interfaces.user;

import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.utils.pagination.Page;
import io.vscale.perpenanto.utils.pagination.PageRequest;

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

    List<Product> getProductsByQuery(String query);
    List<Product> getProductsByRange(Integer start, Integer end);
    Page<Product> getProductsByPage(PageRequest pageRequest);
    Long countProducts();
    Long getMaxPrice();

}
