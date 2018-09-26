package io.vscale.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.dto.implementations.ProductToReservationDTO;
import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.transfermodels.ProductToReservationTransfer;
import io.vscale.perpenanto.models.usermodels.*;
import io.vscale.perpenanto.repositories.interfaces.ProductToReservationRepository;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.services.interfaces.user.ProductToReservationService;
import io.vscale.perpenanto.utils.userutils.CompareAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 30.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToReservationServiceImpl implements ProductToReservationService {

    private ProductToReservationRepository productToReservationRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public ProductToReservationServiceImpl(ProductToReservationRepository productToReservationRepository,
                                           ReservationRepository reservationRepository) {
        this.productToReservationRepository = productToReservationRepository;
        this.reservationRepository = reservationRepository;
    }

    private CompareAttributes<ProductToReservationTransfer> compareAttributes = (oldList, functionMap, sortType) -> {

        List<ProductToReservationTransfer> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                EntityDTOInterface<ProductToReservationTransfer, ProductToReservation> entityDTO =
                                                                                          new ProductToReservationDTO();
                List<ProductToReservationTransfer> productToReservations =
                        entityDTO.convert(this.productToReservationRepository.findAll());
                resultList.addAll(productToReservations);
                break;

            case "reservation":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("reservations")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "product":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("products")).reversed())
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<ProductToReservationTransfer> getProductsToReservations() {
        EntityDTOInterface<ProductToReservationTransfer, ProductToReservation> entityDTO = new ProductToReservationDTO();
        return entityDTO.convert(this.productToReservationRepository.findAll());
    }

    @Override
    public List<ProductToReservationTransfer> getProductsToReservationsByCookie(String cookieValue) {

        List<ProductToReservation> currentProductToReservations = this.productToReservationRepository.findAll();
        EntityDTOInterface<ProductToReservationTransfer, ProductToReservation> entityDTO = new ProductToReservationDTO();
        List<ProductToReservationTransfer> productToReservations = entityDTO.convert(currentProductToReservations);

        int size = 2;

        Function<ProductToReservationTransfer, String> reservationFunction =
                productToReservationTransfer -> String.valueOf(productToReservationTransfer.getReservationId());
        Function<ProductToReservationTransfer, String> productFunction =
                productToReservationTransfer -> String.valueOf(productToReservationTransfer.getProductId());

        List<Function<ProductToReservationTransfer, String>> functions =
                                                                   Arrays.asList(reservationFunction, productFunction);
        List<String> sortTypes = Arrays.asList("reservation", "product");

        Map<String, Function<ProductToReservationTransfer, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEach(i ->{
            String sortType = sortTypes.get(i);
            Function<ProductToReservationTransfer, String> function = functions.get(i);
            functionMap.put(sortType, function);
        });

        return new ArrayList<>(this.compareAttributes.sortList(productToReservations, functionMap, cookieValue));

    }

    @Override
    public void saveProductToReservation(ProductToReservation model) {
        this.productToReservationRepository.save(model);
    }

    @Override
    public void updateProductToReservation(ProductToReservation model) {
        this.productToReservationRepository.update(model);
    }

    @Override
    public void deleteProductToReservation(ProductToReservation model) {
       this.productToReservationRepository.delete(model.getReservation().getId(), model.getProducts().get(0).getId());
    }

    @Override
    public List<Product> getReservationProducts(User user, Timestamp timestamp) {
        Reservation reservation = this.reservationRepository.findByCreatedAt(timestamp);

        return this.productToReservationRepository.find(reservation.getId()).getProducts();

    }

    @Override
    public Integer getReservationPrice(Timestamp timestamp) {

        Reservation reservation = this.reservationRepository.findByCreatedAt(timestamp);
        ProductToReservation productToReservation = this.productToReservationRepository.find(reservation.getId());

        return productToReservation.getProducts().stream()
                                                 .mapToInt(Product::getPrice)
                                                 .sum();
    }
}
