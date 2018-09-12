package io.vscale.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.vscale.perpenanto.models.usermodels.Reservation;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.security.states.ReservationState;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;

/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ReservationRepositoryImpl implements ReservationRepository{

    private JdbcTemplate template;
    private Map<Long, Reservation> reservations = new HashMap<>();

    @Autowired
    public ReservationRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM reservation";
    private static final String INSERT_QUERY = "INSERT INTO reservation(created_at, status) VALUES (?, ?)";
    private static final String FIND_QUERY = "SELECT * FROM reservation WHERE reservation.id = ?";
    private static final String DELETE_QUERY = "DELETE FROM reservation WHERE reservation.id = ?";
    private static final String UPDATE_QUERY = "UPDATE reservation SET(created_at, status) = (?, ?) " +
                                                "WHERE reservation.id = ?";
    private static final String FIND_BY_CREATED_AT_QUERY =
            "SELECT * FROM reservation WHERE reservation.created_at = ?";
    private static final String FIND_BY_STATUS_QUERY =
            "SELECT * FROM reservation WHERE reservation.status = ?";
    private static final String PRICE_QUERY = "SELECT reservation_price(?)";

    private RowMapper<Reservation> reservationRowMapper = (resultSet, rowNumber) -> {

        Long currentReservationId = resultSet.getLong(1);
        String reservationStateString = resultSet.getString(3);

        ReservationState[] reservationStates = ReservationState.values();

        Optional<ReservationState> reservationState =
                Arrays.stream(reservationStates)
                      .filter(reservationState1 -> reservationState1.toString().equals(reservationStateString))
                      .findAny();

        if(!reservationState.isPresent()){
            throw new NullPointerException("reservation state not found!");
        }

        if(this.reservations.get(currentReservationId) == null){

            this.reservations.put(currentReservationId, Reservation.builder()
                                                                   .id(currentReservationId)
                                                                   .createdAt(resultSet.getTimestamp(2))
                                                                   .reservationState(reservationState.get())
                                                                   .build());

        }

        return this.reservations.get(currentReservationId);

    };

    @Override
    public List<Reservation> findAll() {

        this.template.query(FIND_ALL_QUERY, this.reservationRowMapper);

        List<Reservation> result = Lists.newArrayList(this.reservations.values());
        this.reservations.clear();

        return result;

    }

    @Override
    public void save(Reservation model) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.template.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
            ps.setTimestamp(1, model.getCreatedAt());
            ps.setString(2, model.getReservationState().toString());
            return ps;

        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());

    }

    @Override
    public Reservation find(Long id) {

        Reservation reservation = this.template.queryForObject(FIND_QUERY, this.reservationRowMapper, id);
        this.reservations.clear();

        return reservation;
    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(Reservation model) {
        this.template.update(UPDATE_QUERY, model.getCreatedAt(), model.getReservationState().toString(), model.getId());
    }

    @Override
    public List<Reservation> findAllByCreatedAt(Timestamp createdAt) {

        this.template.query(FIND_BY_CREATED_AT_QUERY, this.reservationRowMapper);

        List<Reservation> result = Lists.newArrayList(this.reservations.values());
        this.reservations.clear();

        return result;

    }

    @Override
    public List<Reservation> findAllByStatus(String status) {

        this.template.query(FIND_BY_STATUS_QUERY, this.reservationRowMapper);

        List<Reservation> result = Lists.newArrayList(this.reservations.values());
        this.reservations.clear();

        return result;

    }

    @Override
    public Reservation findByCreatedAt(Timestamp createdAt) {

        Reservation reservation = this.template.queryForObject(FIND_BY_CREATED_AT_QUERY, this.reservationRowMapper,
                                                               createdAt);
        this.reservations.clear();
        return reservation;

    }

    @Override
    public Integer getReservationPrice(Reservation reservation) {
        return this.template.queryForObject(PRICE_QUERY, Integer.class, Math.toIntExact(reservation.getId()));
    }
}
