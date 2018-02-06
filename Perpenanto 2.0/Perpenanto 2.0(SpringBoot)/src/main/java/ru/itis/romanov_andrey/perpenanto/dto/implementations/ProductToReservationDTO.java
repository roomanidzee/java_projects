package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ProductToReservationTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToReservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ProductToReservationDTO implements EntityDTOInterface<ProductToReservationTransfer, ProductToReservation>{

    @Override
    public List<ProductToReservationTransfer> convert(List<ProductToReservation> originalList) {

        List<ProductToReservationTransfer> resultList = new ArrayList<>();

        originalList.forEach(productToReservation -> {

            Reservation reservation = productToReservation.getReservation();
            List<Product> products = productToReservation.getProducts();

            products.stream()
                    .map(product -> ProductToReservationTransfer.builder()
                                                                .reservationId(reservation.getId())
                                                                .productId(product.getId())
                                                                .build()
                    )
                    .forEachOrdered(resultList::add);

        });

        return resultList;

    }
}
