package io.vscale.perpenanto.repositories.interfaces;

import io.vscale.perpenanto.models.usermodels.ProductToUser;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToUserRepository extends CrudDAOInterface<ProductToUser, Long> {

    void delete(Long id1, Long id2);

}
