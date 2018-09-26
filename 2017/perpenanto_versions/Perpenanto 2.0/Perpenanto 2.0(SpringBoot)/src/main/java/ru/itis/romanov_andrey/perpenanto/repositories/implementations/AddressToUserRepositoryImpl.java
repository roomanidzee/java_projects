package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.AddressToUserDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.AddressToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Address;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.AddressToUser;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.AddressToUserRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.ReservationState;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class AddressToUserRepositoryImpl implements AddressToUserRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, AddressToUser> addressesToUsers = new HashMap<>();
    private List<AddressToUserTransfer> addressToUserTransfers = new ArrayList<>();

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM address_to_user LEFT JOIN \"user\" ON \"user\".id = address_to_user.user_id " +
                                          "LEFT JOIN address ON address.id = address_to_user.address_id";
    private static final String INSERT_QUERY = "INSERT INTO address_to_user(user_id, address_id) VALUES(?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM address_to_user LEFT JOIN \"user\" ON \"user\".id = address_to_user.user_id " +
                                          "LEFT JOIN address ON address.id = address_to_user.address_id " +
                                               "WHERE user_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM address_to_user WHERE user_id = ?";
    private static final String DELETE_QUERY_2 = "DELETE FROM address_to_user WHERE user_id = ? AND address_id = ?";

    private RowMapper<AddressToUser> addressToUserRowMapper = (resultSet, rowNumber) -> {

        Long currentUserId = resultSet.getLong(1);

        if(this.addressesToUsers.get(currentUserId) == null){

            this.addressesToUsers.put(currentUserId, AddressToUser.builder()
                                                                  .user(new User())
                                                                  .addresses(Sets.newHashSet())
                                                                  .build());

            String roleString = resultSet.getString(7);
            String userStateString = resultSet.getString(8);

            Role[] roles = Role.values();
            UserState[] userStates = UserState.values();
            ReservationState[] reservationStates = ReservationState.values();

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

            this.addressesToUsers.get(currentUserId).setUser(user);

            Address address = Address.builder()
                                     .id(resultSet.getLong(10))
                                     .country(resultSet.getString(11))
                                     .postalCode(resultSet.getInt(12))
                                     .city(resultSet.getString(13))
                                     .street(resultSet.getString(14))
                                     .homeNumber(resultSet.getInt(15))
                                     .build();

            this.addressesToUsers.get(currentUserId).getAddresses().add(address);
        }else{

            Address address = Address.builder()
                                     .id(resultSet.getLong(10))
                                     .country(resultSet.getString(11))
                                     .postalCode(resultSet.getInt(12))
                                     .city(resultSet.getString(13))
                                     .street(resultSet.getString(14))
                                     .homeNumber(resultSet.getInt(15))
                                     .build();

            this.addressesToUsers.get(currentUserId).getAddresses().add(address);

        }

        return this.addressesToUsers.get(currentUserId);

    };

    private BatchPreparedStatementSetter sqlBatchHelper = new BatchPreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {

            AddressToUserTransfer addressToUserTransfer = addressToUserTransfers.get(i);
            ps.setLong(1, addressToUserTransfer.getUserId());
            ps.setLong(2, addressToUserTransfer.getAddressId());

        }

        @Override
        public int getBatchSize() {
            return addressToUserTransfers.size();
        }
    };

    @Override
    public List<AddressToUser> findAll() {

        this.template.query(FIND_ALL_QUERY, this.addressToUserRowMapper);

        List<AddressToUser> result = Lists.newArrayList(this.addressesToUsers.values());
        this.addressesToUsers.clear();

        return result;

    }

    @Override
    public void save(AddressToUser model) {

        addressToUserTransfers.clear();
        EntityDTOInterface<AddressToUserTransfer, AddressToUser> entityDTO = new AddressToUserDTO();
        addressToUserTransfers.addAll(entityDTO.convert(Lists.newArrayList(model)));

        this.template.batchUpdate(INSERT_QUERY, this.sqlBatchHelper);

    }

    @Override
    public AddressToUser find(Long id) {

        AddressToUser addressToUser = this.template.query(FIND_QUERY, this.addressToUserRowMapper, id).get(0);
        this.addressesToUsers.clear();

        return addressToUser;

    }

    @Override
    public void delete(Long id) {
         this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(AddressToUser model) {

        AddressToUser newModel;

        try{

           newModel = DataAccessUtils.objectResult(Collections.singletonList(find(model.getUser().getId())),
                                                   AddressToUser.class);

            model.getAddresses()
                 .stream()
                 .filter(address -> !newModel.getAddresses().contains(address))
                 .forEachOrdered(newModel.getAddresses()::add);

            delete(model.getUser().getId());
            save(newModel);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){

            AddressToUser addressToUser = AddressToUser.builder()
                                                       .user(model.getUser())
                                                       .addresses(model.getAddresses())
                                                       .build();
            save(addressToUser);

        }

    }

    @Override
    public void delete(Long id1, Long id2) {
        this.template.update(DELETE_QUERY_2, id1, id2);
    }
}
