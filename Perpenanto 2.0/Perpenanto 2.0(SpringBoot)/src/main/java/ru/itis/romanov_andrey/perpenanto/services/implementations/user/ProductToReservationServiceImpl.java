package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.ProductToReservationDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ProductToReservationTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.*;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.BusketRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductToReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductToReservationService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

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

    @Autowired
    private ProductToReservationRepository productToReservationRepository;

    @Autowired
    private BusketRepository busketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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
        List<ProductToReservationTransfer> sortedProductToReservations = new ArrayList<>();

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

        sortedProductToReservations.addAll(this.compareAttributes.sortList(productToReservations, functionMap, cookieValue));
        return sortedProductToReservations;

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
