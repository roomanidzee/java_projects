package ru.itis.romanov_andrey.perpenanto.repositories.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductRepository extends CrudDAOInterface<Product, Long>{

    Product findByTitle(String title);

}
