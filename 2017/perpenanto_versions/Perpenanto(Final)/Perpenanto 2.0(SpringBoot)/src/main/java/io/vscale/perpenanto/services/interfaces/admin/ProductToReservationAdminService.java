package io.vscale.perpenanto.services.interfaces.admin;

import io.vscale.perpenanto.forms.admin.ProductToReservationForm;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProductToReservationAdminService {

    void saveProductToReservation(ProductToReservationForm productToReservationForm);
    void updateProductToReservation(ProductToReservationForm productToReservationForm);
    void deleteProductToReservation(ProductToReservationForm productToReservationForm);

}
