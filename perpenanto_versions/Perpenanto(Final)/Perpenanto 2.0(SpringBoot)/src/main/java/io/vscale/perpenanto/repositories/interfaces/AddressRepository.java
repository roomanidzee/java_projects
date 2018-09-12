package io.vscale.perpenanto.repositories.interfaces;

import io.vscale.perpenanto.models.usermodels.Address;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface AddressRepository extends CrudDAOInterface<Address,Long> {

    Address findByPostalCode(Integer postalCode);

}
