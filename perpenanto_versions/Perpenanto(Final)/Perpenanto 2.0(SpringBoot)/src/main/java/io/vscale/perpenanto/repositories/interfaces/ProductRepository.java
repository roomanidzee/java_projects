package io.vscale.perpenanto.repositories.interfaces;

import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.utils.pagination.Page;
import io.vscale.perpenanto.utils.pagination.PageRequest;

import java.util.List;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductRepository extends CrudDAOInterface<Product, Long>{

    Product findByTitle(String title);
    List<Product> findByUserQuery(String titleQuery);
    List<Product> findByPriceRange(Integer start, Integer end);
    Page<Product> findAll(PageRequest pageRequest);
    Long countAllProducts();
    Long getMaxPrice();

}
