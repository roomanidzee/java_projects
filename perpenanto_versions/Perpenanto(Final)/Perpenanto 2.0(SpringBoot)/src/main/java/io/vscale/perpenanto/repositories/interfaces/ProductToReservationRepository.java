package io.vscale.perpenanto.repositories.interfaces;

import io.vscale.perpenanto.models.usermodels.ProductToReservation;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToReservationRepository extends CrudDAOInterface<ProductToReservation, Long> {

    void delete(Long id1, Long id2);

}
