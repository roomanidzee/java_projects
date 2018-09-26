package io.vscale.perpenanto.services.implementations.admin;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.forms.admin.ProductToReservationForm;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.ProductToReservation;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.repositories.interfaces.ProductToReservationRepository;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.services.interfaces.admin.ProductToReservationAdminService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToReservationAdminServiceImpl implements ProductToReservationAdminService{

    private final ProductToReservationRepository productToReservationRepository;

    private final ReservationRepository reservationRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ProductToReservationAdminServiceImpl(ProductToReservationRepository productToReservationRepository,
                                                ReservationRepository reservationRepository,
                                                ProductRepository productRepository) {
        this.productToReservationRepository = productToReservationRepository;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void saveProductToReservation(ProductToReservationForm productToReservationForm) {

        Reservation reservation = this.reservationRepository.find(productToReservationForm.getOrderId());
        Product product = this.productRepository.find(productToReservationForm.getProductId());

        ProductToReservation productToReservation = ProductToReservation.builder()
                                                                        .reservation(reservation)
                                                                        .products(Lists.newArrayList(product))
                                                                        .build();
        this.productToReservationRepository.save(productToReservation);

    }

    @Override
    public void updateProductToReservation(ProductToReservationForm productToReservationForm) {

        Reservation reservation = this.reservationRepository.find(productToReservationForm.getOrderId());
        Product product = this.productRepository.find(productToReservationForm.getProductId());

        ProductToReservation productToReservation = ProductToReservation.builder()
                                                                        .reservation(reservation)
                                                                        .products(Lists.newArrayList(product))
                                                                        .build();

        this.productToReservationRepository.update(productToReservation);

    }

    @Override
    public void deleteProductToReservation(ProductToReservationForm productToReservationForm) {
        this.productToReservationRepository.delete(
                productToReservationForm.getOrderId(), productToReservationForm.getProductId()
        );
    }
}
