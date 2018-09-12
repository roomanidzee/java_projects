package io.vscale.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import io.vscale.perpenanto.dto.implementations.ReservationToUserDTO;
import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.transfermodels.ReservationToUserTransfer;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.models.usermodels.ReservationToUser;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.ReservationToUserRepository;
import io.vscale.perpenanto.security.role.Role;
import io.vscale.perpenanto.security.states.ReservationState;
import io.vscale.perpenanto.security.states.UserState;

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
public class ReservationToUserRepositoryImpl implements ReservationToUserRepository{

    private JdbcTemplate template;
    private Map<Long, ReservationToUser> reservationsToUsers = new HashMap<>();
    private List<ReservationToUserTransfer> reservationsToUserTransfers = new ArrayList<>();

    @Autowired
    public ReservationToUserRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM reservation_to_user " +
                    "LEFT JOIN \"user\" ON \"user\".id = reservation_to_user.user_id " +
                    "LEFT JOIN reservation ON reservation.id = reservation_to_user.reservation_id";
    private static final String INSERT_QUERY =
            "INSERT INTO reservation_to_user(user_id, reservation_id) VALUES(?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM reservation_to_user " +
                    "LEFT JOIN \"user\" ON \"user\".id = reservation_to_user.user_id " +
                    "LEFT JOIN reservation ON reservation.id = reservation_to_user.reservation_id " +
                    "WHERE reservation_to_user.user_id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM reservation_to_user WHERE reservation_to_user.user_id = ?";
    private static final String DELETE_QUERY_2 =
            "DELETE FROM reservation_to_user WHERE user_id = ? AND reservation_id = ?";

    private RowMapper<ReservationToUser> reservationToUserRowMapper = (resultSet, rowNumber) -> {

        Long currentUserId = resultSet.getLong(1);

        String roleString = resultSet.getString(7);
        String userStateString = resultSet.getString(8);
        String stateString = resultSet.getString(12);

        Role[] roles = Role.values();
        UserState[] userStates = UserState.values();
        ReservationState[] reservationStates = ReservationState.values();

        Optional<Role> role = Arrays.stream(roles)
                                    .filter(role1 -> role1.toString().equals(roleString))
                                    .findAny();

        Optional<UserState> userState = Arrays.stream(userStates)
                                              .filter(userState1 -> userState1.toString().equals(userStateString))
                                              .findAny();

        Optional<ReservationState> reservationState =
                Arrays.stream(reservationStates)
                      .filter(reservationState1 -> reservationState1.toString().equals(stateString))
                      .findAny();

        if(!role.isPresent()){
            throw new NullPointerException("role not found!");
        }

        if(!userState.isPresent()){
            throw new NullPointerException("user state not found!");
        }

        if(!reservationState.isPresent()){
            throw new NullPointerException("reservation state not found!");
        }

        if(this.reservationsToUsers.get(currentUserId) == null){

            this.reservationsToUsers.put(currentUserId, ReservationToUser.builder()
                                                                         .user(new User())
                                                                         .reservations(Sets.newHashSet())
                                                                         .build());

            User user = User.builder()
                            .id(resultSet.getLong(3))
                            .login(resultSet.getString(4))
                            .password(resultSet.getString(5))
                            .tempPassword(resultSet.getString(6))
                            .role(role.get())
                            .userState(userState.get())
                            .confirmHash(resultSet.getString(9))
                            .build();

            this.reservationsToUsers.get(currentUserId).setUser(user);

            Reservation reservation = Reservation.builder()
                                                 .id(resultSet.getLong(10))
                                                 .createdAt(resultSet.getTimestamp(11))
                                                 .reservationState(reservationState.get())
                                                 .build();

            this.reservationsToUsers.get(currentUserId).getReservations().add(reservation);

        }else{

            Reservation reservation = Reservation.builder()
                                                 .id(resultSet.getLong(10))
                                                 .createdAt(resultSet.getTimestamp(11))
                                                 .reservationState(reservationState.get())
                                                 .build();

            this.reservationsToUsers.get(currentUserId).getReservations().add(reservation);

        }

        return this.reservationsToUsers.get(currentUserId);

    };

    private BatchPreparedStatementSetter sqlBatchHelper = new BatchPreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {

            ReservationToUserTransfer reservationToUserTransfer = reservationsToUserTransfers.get(i);
            ps.setLong(1, reservationToUserTransfer.getUserId());
            ps.setLong(2, reservationToUserTransfer.getReservationId());

        }

        @Override
        public int getBatchSize() {
            return reservationsToUserTransfers.size();
        }
    };

    @Override
    public List<ReservationToUser> findAll() {

        this.template.query(FIND_ALL_QUERY, this.reservationToUserRowMapper);

        List<ReservationToUser> result = Lists.newArrayList(this.reservationsToUsers.values());
        this.reservationsToUsers.clear();

        return result;

    }

    @Override
    public void save(ReservationToUser model) {

        reservationsToUserTransfers.clear();
        EntityDTOInterface<ReservationToUserTransfer, ReservationToUser> entityDTO = new ReservationToUserDTO();
        reservationsToUserTransfers.addAll(entityDTO.convert(Lists.newArrayList(model)));

        this.template.batchUpdate(INSERT_QUERY, this.sqlBatchHelper);

    }

    @Override
    public ReservationToUser find(Long id) {

        ReservationToUser reservationToUser =
                this.template.query(FIND_QUERY, this.reservationToUserRowMapper, id).get(0);
        this.reservationsToUsers.clear();

        return reservationToUser;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(ReservationToUser model) {

        ReservationToUser newModel = find(model.getUser().getId());

        model.getReservations()
             .stream()
             .filter(reservation -> !newModel.getReservations().contains(reservation))
             .forEach(newModel.getReservations()::add);

        delete(model.getUser().getId());
        save(newModel);

    }

    @Override
    public void delete(Long id1, Long id2) {
        this.template.update(DELETE_QUERY_2, id1, id2);
    }
}
