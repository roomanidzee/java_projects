package io.vscale.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import io.vscale.perpenanto.dto.implementations.ProductToUserDTO;
import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.transfermodels.ProductToUserTransfer;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.models.usermodels.ProductToUser;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.ProductToUserRepository;
import io.vscale.perpenanto.security.role.Role;
import io.vscale.perpenanto.security.states.UserState;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

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
public class ProductToUserRepositoryImpl implements ProductToUserRepository{

    private JdbcTemplate template;
    private JdbcTemplateWrapper<ProductToUser> jdbcTemplateWrapper;

    @Autowired
    public ProductToUserRepositoryImpl(JdbcTemplate template, JdbcTemplateWrapper<ProductToUser> jdbcTemplateWrapper) {
        this.template = template;
        this.jdbcTemplateWrapper = jdbcTemplateWrapper;
    }

    private Map<Long, ProductToUser> productsToUsers = new HashMap<>();
    private List<ProductToUserTransfer> productToUserTransfers = new ArrayList<>();

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM product_to_user " +
                    "LEFT JOIN \"user\" ON \"user\".id = product_to_user.user_id " +
                    "LEFT JOIN product ON product.id = product_to_user.product_id ";
    private static final String INSERT_QUERY =
            "INSERT INTO product_to_user(user_id, product_id) VALUES(?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM product_to_user " +
                    "LEFT JOIN \"user\" ON \"user\".id = product_to_user.user_id " +
                    "LEFT JOIN product ON product.id = product_to_user.product_id " +
                    "WHERE product_to_user.user_id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM product_to_user WHERE product_to_user.user_id = ?";
    private static final String DELETE_QUERY_2 =
            "DELETE FROM product_to_user WHERE user_id = ? AND product_id = ?";

    private RowMapper<ProductToUser> productToUserRowMapper = (resultSet, rowNumber) -> {

        Long currentUserId = resultSet.getLong(1);

        if(this.productsToUsers.get(currentUserId) == null){

            this.productsToUsers.put(currentUserId, ProductToUser.builder()
                                                                 .user(new User())
                                                                 .products(Sets.newHashSet())
                                                                 .build());

            String roleString = resultSet.getString(7);
            String userStateString = resultSet.getString(8);

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
                            .id(resultSet.getLong(3))
                            .login(resultSet.getString(4))
                            .password(resultSet.getString(5))
                            .tempPassword(resultSet.getString(6))
                            .role(role.get())
                            .userState(userState.get())
                            .confirmHash(resultSet.getString(9))
                            .build();
            this.productsToUsers.get(currentUserId).setUser(user);

            Product product = Product.builder()
                                     .id(resultSet.getLong(10))
                                     .title(resultSet.getString(11))
                                     .price(resultSet.getInt(12))
                                     .description(resultSet.getString(13))
                                     .photoLink(resultSet.getString(14))
                                     .build();
            this.productsToUsers.get(currentUserId).getProducts().add(product);

        }else{

            Product product = Product.builder()
                                     .id(resultSet.getLong(10))
                                     .title(resultSet.getString(11))
                                     .price(resultSet.getInt(12))
                                     .description(resultSet.getString(13))
                                     .photoLink(resultSet.getString(14))
                                     .build();
            this.productsToUsers.get(currentUserId).getProducts().add(product);

        }

        return this.productsToUsers.get(currentUserId);

    };

    private BatchPreparedStatementSetter sqlBatchHelper = new BatchPreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {

            ProductToUserTransfer productToUserTransfer = productToUserTransfers.get(i);
            ps.setLong(1, productToUserTransfer.getUserId());
            ps.setLong(2, productToUserTransfer.getProductId());

        }

        @Override
        public int getBatchSize() {
            return productToUserTransfers.size();
        }
    };

    @Override
    public List<ProductToUser> findAll() {

        this.template.query(FIND_ALL_QUERY, this.productToUserRowMapper);

        List<ProductToUser> result = Lists.newArrayList(this.productsToUsers.values());
        this.productsToUsers.clear();

        return result;

    }

    @Override
    public void save(ProductToUser model) {

        productToUserTransfers.clear();
        EntityDTOInterface<ProductToUserTransfer, ProductToUser> entityDTO = new ProductToUserDTO();
        productToUserTransfers.addAll(entityDTO.convert(Lists.newArrayList(model)));

        this.template.batchUpdate(INSERT_QUERY, this.sqlBatchHelper);

    }

    @Override
    public ProductToUser find(Long id) {

        ProductToUser productToUser = this.template.queryForObject(FIND_QUERY, this.productToUserRowMapper, id);
        this.productsToUsers.clear();

        return productToUser;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(ProductToUser model) {

        Optional<ProductToUser> tempProductToUser =
                this.jdbcTemplateWrapper.findItem(ProductToUserRepositoryImpl.this, model.getUser().getId());

        if(!tempProductToUser.isPresent()){

            ProductToUser newModel = ProductToUser.builder()
                                                  .user(model.getUser())
                                                  .products(model.getProducts())
                                                  .build();
            save(newModel);

        }

        tempProductToUser.ifPresent(productToUser -> {

            model.getProducts()
                 .stream()
                 .filter(product -> !productToUser.getProducts().contains(product))
                 .forEach(productToUser.getProducts()::add);

            delete(model.getUser().getId());
            save(productToUser);

        });

    }

    @Override
    public void delete(Long id1, Long id2) {
        this.template.update(DELETE_QUERY_2, id1, id2);
    }
}
