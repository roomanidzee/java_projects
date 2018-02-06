package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.BusketDTO;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.ProductToReservationDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.BusketTransfer;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ProductToReservationTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.*;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.BusketRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 27.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class BusketRepositoryImpl implements BusketRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, Busket> buskets = new HashMap<>();
    private List<BusketTransfer> busketTransfers = new ArrayList<>();
    private List<ProductToReservationTransfer> productToReservationTransfers = new ArrayList<>();

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM busket LEFT JOIN profile ON profile.id = busket.busket_user_id " +
                                 "LEFT JOIN \"user\" ON \"user\".id = profile.user_id " +
                                 "LEFT JOIN product ON product.id = busket.busket_product_id";
    private static final String INSERT_QUERY =
            "INSERT INTO busket(busket_user_id, busket_product_id) VALUES(?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM busket LEFT JOIN profile ON profile.id = busket.busket_user_id " +
                                 "LEFT JOIN \"user\" ON \"user\".id = profile.user_id " +
                                 "LEFT JOIN product ON product.id = busket.busket_product_id " +
                                 "WHERE busket.busket_user_id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM busket WHERE busket.busket_user_id = ?";
    private static final String DELETE_QUERY_2 =
            "DELETE FROM busket WHERE busket.busket_user_id = ? AND busket.busket_product_id = ?";

    //запросы для реализации оплаты заказа
    private static final String PAY_QUERY_1 =
            "INSERT INTO reservation_to_user(user_id, reservation_id) VALUES(?, ?)";
    private static final String PAY_QUERY_2 =
            "INSERT INTO product_to_reservation(reservation_id, product_id) VALUES(?, ?)";


    private RowMapper<Busket> busketRowMapper = (resultSet, rowNumber) -> {

        Long currentUserId = resultSet.getLong(1);

        if(this.buskets.get(currentUserId) == null){

            this.buskets.put(currentUserId, Busket.builder()
                                                  .profile(new Profile())
                                                  .products(Lists.newArrayList())
                                                  .build());

            String roleString = resultSet.getString(13);
            String userStateString = resultSet.getString(14);

            Role[] roles = Role.values();
            UserState[] userStates = UserState.values();

            Optional<Role> role = Arrays.stream(roles)
                                        .filter(role1 -> role1.toString().equals(roleString))
                                        .findAny();

            Optional<UserState> userState = Arrays.stream(userStates)
                                                  .filter(userState1 -> userState1.toString().equals(userStateString))
                                                  .findAny();

            if(!role.isPresent()){
                throw new NullPointerException("role not found!");
            }

            if(!userState.isPresent()){
                throw new NullPointerException("user state not found!");
            }

            User user = User.builder()
                            .id(resultSet.getLong(9))
                            .login(resultSet.getString(10))
                            .password(resultSet.getString(11))
                            .tempPassword(resultSet.getString(12))
                            .role(role.get())
                            .userState(userState.get())
                            .confirmHash(resultSet.getString(15))
                            .build();

            Profile profile = Profile.builder()
                                     .id(resultSet.getLong(3))
                                     .user(user)
                                     .email(resultSet.getString(5))
                                     .name(resultSet.getString(6))
                                     .surname(resultSet.getString(7))
                                     .phone(resultSet.getString(8))
                                     .build();

            this.buskets.get(currentUserId).setProfile(profile);

            Product product = Product.builder()
                                     .id(resultSet.getLong(16))
                                     .title(resultSet.getString(17))
                                     .price(resultSet.getInt(18))
                                     .description(resultSet.getString(19))
                                     .photoLink(resultSet.getString(20))
                                     .build();
            this.buskets.get(currentUserId).getProducts().add(product);


        }else{

            Product product = Product.builder()
                                     .id(resultSet.getLong(16))
                                     .title(resultSet.getString(17))
                                     .price(resultSet.getInt(18))
                                     .description(resultSet.getString(19))
                                     .photoLink(resultSet.getString(20))
                                     .build();
            this.buskets.get(currentUserId).getProducts().add(product);

        }

        return this.buskets.get(currentUserId);

    };

    private BatchPreparedStatementSetter busketBatchHelper = new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {

            BusketTransfer busketTransfer = busketTransfers.get(i);
            ps.setLong(1, busketTransfer.getProfileId());
            ps.setLong(2, busketTransfer.getProductId());

        }

        @Override
        public int getBatchSize() {
            return busketTransfers.size();
        }
    };

    private BatchPreparedStatementSetter reservationBatchHelper = new BatchPreparedStatementSetter() {
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
    public List<Busket> findAll() {

        this.template.query(FIND_ALL_QUERY, this.busketRowMapper);

        List<Busket> result = Lists.newArrayList(this.buskets.values());
        this.buskets.clear();

        return result;

    }

    @Override
    public void save(Busket model) {

        busketTransfers.clear();
        EntityDTOInterface<BusketTransfer, Busket> entityDTO = new BusketDTO();
        busketTransfers.addAll(entityDTO.convert(Lists.newArrayList(model)));

        this.template.batchUpdate(INSERT_QUERY, this.busketBatchHelper);

    }

    @Override
    public Busket find(Long id) {

        Busket busket = this.template.query(FIND_QUERY, this.busketRowMapper, id).get(0);
        this.buskets.clear();

        return busket;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(Busket model) {

        Busket newModel;

        try{
            newModel =
                      DataAccessUtils.objectResult(Collections.singletonList(find(model.getProfile().getId())),
                                                   Busket.class);

            List<Product> products = model.getProducts();
            List<Product> oldProducts = new ArrayList<>(newModel.getProducts());

            products.stream()
                    .filter(product -> !oldProducts.contains(product))
                    .forEach(oldProducts::add);

            newModel.setProducts(oldProducts);
            delete(model.getProfile().getId());
            save(newModel);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){

            Busket tempModel = Busket.builder()
                                     .profile(model.getProfile())
                                     .products(model.getProducts())
                                     .build();
            save(tempModel);

        }

    }

    @Override
    public void delete(Long id1, Long id2) {
        this.template.update(DELETE_QUERY_2, id1, id2);
    }

    @Override
    public void payForBusket(Busket busket, Reservation reservation) {

        User user = busket.getProfile().getUser();
        List<Product> products = busket.getProducts();

        ProductToReservation productToReservation = ProductToReservation.builder()
                                                                        .reservation(reservation)
                                                                        .products(Lists.newArrayList())
                                                                        .build();

        this.template.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(PAY_QUERY_1);
            ps.setLong(1, user.getId());
            ps.setLong(2, reservation.getId());
            return ps;

        });

        products.forEach(productToReservation.getProducts()::add);

        productToReservationTransfers.clear();
        EntityDTOInterface<ProductToReservationTransfer, ProductToReservation> entityDTO =
                new ProductToReservationDTO();

        productToReservationTransfers.addAll(entityDTO.convert(Lists.newArrayList(productToReservation)));
        this.template.batchUpdate(PAY_QUERY_2, this.reservationBatchHelper);
        delete(busket.getProfile().getId());

    }
}
