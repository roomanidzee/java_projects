package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductToReservationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToReservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductToReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductToReservationAdminService;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToReservationAdminServiceImpl implements ProductToReservationAdminService{

    @Autowired
    private ProductToReservationRepository productToReservationRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductRepository productRepository;

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
