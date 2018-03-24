package ru.itis.romanov_andrey.perpenanto.repositories.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToUser;

import java.util.List;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToUserRepository extends CrudDAOInterface<ProductToUser, Long>{

    void delete(Long id1, Long id2);

}
