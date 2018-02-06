package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.ProductToReservationDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ProductToReservationTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToReservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductToReservationRepository;
import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 28.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ProductToReservationRepositoryImpl implements ProductToReservationRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, ProductToReservation> productsToReservations = new HashMap<>();
    private List<ProductToReservationTransfer> productToReservationTransfers = new ArrayList<>();

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM product_to_reservation " +
                    "LEFT JOIN reservation ON reservation.id = product_to_reservation.reservation_id " +
                    "LEFT JOIN product ON product.id = product_to_reservation.product_id";
    private static final String INSERT_QUERY =
            "INSERT INTO product_to_reservation(reservation_id, product_id) VALUES(?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM product_to_reservation " +
                    "LEFT JOIN reservation ON reservation.id = product_to_reservation.reservation_id " +
                    "LEFT JOIN product ON product.id = product_to_reservation.product_id " +
                    "WHERE product_to_reservation.reservation_id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM product_to_reservation WHERE product_to_reservation.reservation_id = ?";
    private static final String DELETE_QUERY_2 =
            "DELETE FROM product_to_reservation WHERE reservation_id = ? AND product_id = ?";

    private RowMapper<ProductToReservation> productToReservationRowMapper = (resultSet, rowNumber) -> {

        Long currentReservationId = resultSet.getLong(1);

        if(this.productsToReservations.get(currentReservationId) == null){

            this.productsToReservations.put(currentReservationId, ProductToReservation.builder()
                                                                                      .reservation(new Reservation())
                                                                                      .products(Lists.newArrayList())
                                                                                      .build());

            String stateString = resultSet.getString(5);
            ReservationState[] reservationStates = ReservationState.values();

            Optional<ReservationState> reservationState =
                    Arrays.stream(reservationStates)
                          .filter(reservationState1 -> reservationState1.toString().equals(stateString))
                          .findAny();

            if(!reservationState.isPresent()){
                throw new NullPointerException("reservation state not found!");
            }

            Reservation reservation = Reservation.builder()
                                                 .id(resultSet.getLong(3))
                                                 .createdAt(resultSet.getTimestamp(4))
                                                 .reservationState(reservationState.get())
                                                 .build();

            this.productsToReservations.get(currentReservationId).setReservation(reservation);

            Product product = Product.builder()
                                     .id(resultSet.getLong(6))
                                     .title(resultSet.getString(7))
                                     .price(resultSet.getInt(8))
                                     .description(resultSet.getString(9))
                                     .photoLink(resultSet.getString(10))
                                     .build();
            this.productsToReservations.get(currentReservationId).getProducts().add(product);

        }else{

            Product product = Product.builder()
                                     .id(resultSet.getLong(6))
                                     .title(resultSet.getString(7))
                                     .price(resultSet.getInt(8))
                                     .description(resultSet.getString(9))
                                     .photoLink(resultSet.getString(10))
                                     .build();
            this.productsToReservations.get(currentReservationId).getProducts().add(product);

        }

        return this.productsToReservations.get(currentReservationId);

    };

    private BatchPreparedStatementSetter sqlBatchHelper = new BatchPreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {

            ProductToReservationTransfer productToReservationTransfer = productToReservationTransfers.get(i);
            ps.setLong(1, productToReservationTransfer.getReservationId());
            ps.setLong(2, productToReservationTransfer.getProductId());

        }

        @Override
        public int getBatchSize() {
            return productToReservationTransfers.size();
        }
    };

    @Override
    public List<ProductToReservation> findAll() {

        this.template.query(FIND_ALL_QUERY, this.productToReservationRowMapper);

        List<ProductToReservation> result = Lists.newArrayList(this.productsToReservations.values());
        this.productsToReservations.clear();

        return result;

    }

    @Override
    public void save(ProductToReservation model) {

        productToReservationTransfers.clear();
        EntityDTOInterface<ProductToReservationTransfer, ProductToReservation> entityDTO =
                                                                                          new ProductToReservationDTO();
        productToReservationTransfers.addAll(entityDTO.convert(Lists.newArrayList(model)));

        this.template.batchUpdate(INSERT_QUERY, this.sqlBatchHelper);

    }

    @Override
    public ProductToReservation find(Long id) {

        ProductToReservation productToReservation =
                this.template.query(FIND_QUERY, this.productToReservationRowMapper, id).get(0);
        this.productsToReservations.clear();

        return productToReservation;

    }

    @Override
    public void delete(Long id) {

        this.template.update(DELETE_QUERY, id);

    }

    @Override
    public void update(ProductToReservation model) {

        ProductToReservation newModel = find(model.getReservation().getId());

        model.getProducts()
             .stream()
             .filter(product -> !newModel.getProducts().contains(product))
             .forEach(newModel.getProducts()::add);

        delete(model.getReservation().getId());
        save(newModel);

    }

    @Override
    public void delete(Long id1, Long id2) {
        this.template.update(DELETE_QUERY_2, id1, id2);
    }
}
