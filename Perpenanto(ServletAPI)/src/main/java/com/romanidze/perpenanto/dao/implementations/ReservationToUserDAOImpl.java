package com.romanidze.perpenanto.dao.implementations;

import com.google.common.collect.Lists;
import com.romanidze.perpenanto.dao.interfaces.ReservationDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ReservationToUserDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.UserDAOInterface;
import com.romanidze.perpenanto.models.Reservation;
import com.romanidze.perpenanto.models.ReservationToUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ReservationToUserDAOImpl implements ReservationToUserDAOInterface{

    private Connection conn;
    private UserDAOInterface userDAO;
    private ReservationDAOInterface reservationDAO;

    private static final String INSERT_QUERY = "INSERT INTO reservation_to_user(user_id, user_reservation_id) " +
                                                "VALUES(?, ?)";
    private static final String FIND_QUERY = "SELECT FROM reservation_to_user WHERE reservation_to_user.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM reservation_to_user";
    private static final String DELETE_QUERY = "DELETE FROM reservation_to_user WHERE reservation_to_user.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE reservation_to_user SET(user_id, user_reservation_id) = (?, ?) " +
                    "WHERE reservation_to_user.id = ?";
    private static final String FIND_BY_USER_QUERY = "SELECT * FROM reservation_to_user " +
                                                      "WHERE reservation_to_user.user_id = ?";

    private ReservationToUserDAOImpl(){}

    public ReservationToUserDAOImpl(Connection conn){

        this.conn = conn;
        this.userDAO = new UserDAOImpl(conn);
        this.reservationDAO = new ReservationDAOImpl(conn);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ReservationToUser> findAll() {

        List<ReservationToUser> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                ReservationToUser reservationToUser = ReservationToUser.builder()
                                                                       .id(rs.getLong(1))
                                                                       .user(this.userDAO.find(rs.getLong(2)))
                                                                       .userReservations(Lists.newArrayList())
                                                                       .build();
                reservationToUser.getUserReservations().add(this.reservationDAO.find(rs.getLong(3)));
                resultList.add(reservationToUser);

            }

        }catch (SQLException e) {

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(ReservationToUser model) {

        int count = model.getUserReservations().size();
        List<Reservation> reservations = model.getUserReservations();

        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[]{"id"})){

            IntStream.range(0, count).forEachOrdered(i -> {

                try{

                    ps.setLong(1, model.getUser().getId());
                    ps.setLong(2, reservations.get(i).getId());
                    ps.addBatch();

                }catch (SQLException e) {
                    e.printStackTrace();
                }

            });

            ps.executeBatch();
            this.conn.commit();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    Long id = rs.getLong(1);
                    model.setId(id);
                }

            }

        }catch (SQLException e) {
            try {
                this.conn.rollback();
                this.conn.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                this.conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public ReservationToUser find(Long id) {

        ReservationToUser reservationToUser = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                reservationToUser = ReservationToUser.builder()
                                                     .id(rs.getLong(1))
                                                     .user(this.userDAO.find(rs.getLong(2)))
                                                     .userReservations(Lists.newArrayList())
                                                     .build();
                reservationToUser.getUserReservations().add(this.reservationDAO.find(rs.getLong(3)));

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationToUser;

    }

    @Override
    public void delete(Long id) {

        try (PreparedStatement ps = this.conn.prepareStatement(DELETE_QUERY)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(ReservationToUser model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setLong(1, model.getUser().getId());
            ps.setLong(2, model.getUserReservations().get(0).getId());
            ps.setLong(3, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ReservationToUser> findAllByUserId(Long userId) {

        List<ReservationToUser> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_USER_QUERY)){

            ps.setLong(1, userId);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){

                    ReservationToUser reservationToUser = ReservationToUser.builder()
                                                                           .id(rs.getLong(1))
                                                                           .user(this.userDAO.find(rs.getLong(2)))
                                                                           .userReservations(Lists.newArrayList())
                                                                           .build();
                    reservationToUser.getUserReservations().add(this.reservationDAO.find(rs.getLong(3)));
                    resultList.add(reservationToUser);

                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }
}
