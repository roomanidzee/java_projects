package io.vscale.perpenanto.utils.userutils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.Reservation;

import java.util.List;
import java.util.Map;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ReservationInformation {

    private Reservation reservation;
    private String prettyReservationDate;
    private Integer price;
    private List<Product> products;
    private Map<Product, Long> productsCount;

}
