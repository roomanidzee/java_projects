package io.vscale.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.security.role.Role;
import io.vscale.perpenanto.security.states.UserState;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.Arrays;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class UserRepositoryImpl implements UserRepository{

    private JdbcTemplate template;
    private JdbcTemplateWrapper<User> jdbcTemplateWrapper;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate template, JdbcTemplateWrapper<User> jdbcTemplateWrapper) {
        this.template = template;
        this.jdbcTemplateWrapper = jdbcTemplateWrapper;
    }

    private Map<Long, User> users = new HashMap<>();

    private static final String FIND_ALL_QUERY = "SELECT * FROM \"user\"";
    private static final String INSERT_QUERY =
            "INSERT INTO \"user\"(login, password, password_temp, role, user_state, unique_confirm_hash)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT * FROM \"user\" WHERE \"user\".id = ?";
    private static final String DELETE_QUERY = "DELETE FROM \"user\" WHERE \"user\".id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE \"user\" " +
                    "SET(login, password, password_temp, role, user_state, unique_confirm_hash) = (?, ?, ?, ?, ?, ?)" +
                    " WHERE \"user\".id = ?";
    private static final String FIND_BY_LOGIN_QUERY =
            "SELECT * FROM \"user\" WHERE \"user\".login = ?";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM \"user\" WHERE \"user\".id = ?";
    private static final String FIND_BY_HASH_QUERY =
            "SELECT * FROM \"user\" WHERE \"user\".unique_confirm_hash = ?";
    private static final String FIND_BY_ROLE_QUERY =
            "SELECT * FROM \"user\" WHERE \"user\".role = ?";
    private static final String PRODUCTS_QUERY = "SELECT solded_products_count(?)";
    private static final String MONEY_QUERY = "SELECT spended_money_on_reservations(?)";
    private static final String RESERVATION_COUNT_QUERY = "SELECT count_reservations(?)";
    private static final String COMMON_PRODUCTS_PRICE_QUERY = "SELECT get_common_products_price(?)";

    private RowMapper<User> userRowMapper = (resultSet, rowNumber) -> {

        Long currentUserId = resultSet.getLong(1);
        String roleString = resultSet.getString(5);
        String userStateString = resultSet.getString(6);

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

        if(this.users.get(currentUserId) == null){

            this.users.put(currentUserId, User.builder()
                                              .id(currentUserId)
                                              .login(resultSet.getString(2))
                                              .password(resultSet.getString(3))
                                              .tempPassword(resultSet.getString(4))
                                              .role(role.get())
                                              .userState(userState.get())
                                              .confirmHash(resultSet.getString(7))
                                              .build());

        }

        return this.users.get(currentUserId);

    };

    @Override
    public List<User> findAll() {

        this.template.query(FIND_ALL_QUERY, this.userRowMapper);

        List<User> result = Lists.newArrayList(this.users.values());
        this.users.clear();

        return result;

    }

    @Override
    public void save(User model) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.template.update(connection -> {

                    PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
                    ps.setString(1, model.getLogin());
                    ps.setString(2, model.getPassword());
                    ps.setString(3, model.getTempPassword());
                    ps.setString(4, model.getRole().toString());
                    ps.setString(5, model.getUserState().toString());
                    ps.setString(6, model.getConfirmHash());
                    return ps;

                }, keyHolder);

        model.setId(keyHolder.getKey().longValue());

    }

    @Override
    public User find(Long id) {

        User user = this.template.queryForObject(FIND_QUERY, this.userRowMapper, id);
        this.users.clear();

        return user;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(User model) {
         this.template.update(UPDATE_QUERY, model.getLogin(), model.getPassword(), model.getTempPassword(),
                                            model.getRole().toString(), model.getUserState().toString(),
                                            model.getConfirmHash(), model.getId());
    }

    @Override
    public Optional<User> findByLogin(String login) {

        Optional<User> user = this.jdbcTemplateWrapper.findByItem(FIND_BY_LOGIN_QUERY, this.userRowMapper, login);
        this.users.clear();

        if(!user.isPresent()){
            throw new NullPointerException();
        }

        return user;

    }

    @Override
    public Optional<User> findById(Long userId) {

        Optional<User> user = this.jdbcTemplateWrapper.findByItem(FIND_BY_ID_QUERY, this.userRowMapper, userId);
        this.users.clear();

        if(!user.isPresent()){
            throw new NullPointerException();
        }

        return user;
    }

    @Override
    public Optional<User> findByConfirmHash(String confirmHash) {

        Optional<User> user = this.jdbcTemplateWrapper.findByItem(FIND_BY_HASH_QUERY, this.userRowMapper, confirmHash);
        this.users.clear();

        if(!user.isPresent()){
            throw new NullPointerException();
        }

        return user;
    }

    @Override
    public List<User> findAllByRole(Role role) {

        this.template.query(FIND_BY_ROLE_QUERY, this.userRowMapper, role.toString());

        List<User> result = Lists.newArrayList(this.users.values());
        this.users.clear();

        return result;

    }

    @Override
    public Integer getSoldProductsCount(User user) {
        return this.template.queryForObject(PRODUCTS_QUERY, Integer.class, Math.toIntExact(user.getId()));
    }

    @Override
    public Integer getSpentMoneyOnReservations(User user) {
        return this.template.queryForObject(MONEY_QUERY, Integer.class, Math.toIntExact(user.getId()));
    }

    @Override
    public Integer getReservationCount(User user) {
        return this.template.queryForObject(RESERVATION_COUNT_QUERY, Integer.class, Math.toIntExact(user.getId()));
    }

    @Override
    public Integer getCommonProductPrice(User user) {
        return this.template.queryForObject(COMMON_PRODUCTS_PRICE_QUERY, Integer.class, Math.toIntExact(user.getId()));
    }
}
