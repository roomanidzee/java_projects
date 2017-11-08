package com.romanidze.perpenanto.dao.implementations;

import com.romanidze.perpenanto.dao.interfaces.ReservationDAOInterface;
import com.romanidze.perpenanto.models.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAOInterface {

    private Connection conn;

    private static final String INSERT_QUERY = "INSERT INTO reservation(created_at, status) VALUES(?, ?)";
    private static final String FIND_QUERY = "SELECT FROM reservation WHERE reservation.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM reservation";
    private static final String DELETE_QUERY = "DELETE FROM reservation WHERE reservation.id = ?";
    private static final String UPDATE_QUERY = "UPDATE reservation SET(created_at, status) = (?, ?) " +
                                                "WHERE reservation.id = ?";
    private static final String FIND_BY_TIMESTAMP = "SELECT FROM reservation WHERE reservation.created_at = ?";

    private ReservationDAOImpl(){}

    public ReservationDAOImpl(Connection conn){

        this.conn = conn;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Reservation> findAll() {

        List<Reservation> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()){

           while(rs.next()){

               Reservation reservation = Reservation.builder()
                                                    .id(rs.getLong(1))
                                                    .createdAt(rs.getTimestamp(2))
                                                    .status(rs.getString(3))
                                                    .build();
               resultList.add(reservation);

           }

        }catch(SQLException e){

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(Reservation model) {

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[] {"id"})){

            ps.setTimestamp(1, model.getCreatedAt());
            ps.setString(2, model.getStatus());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){

                if(rs.next()){

                    Long id = rs.getLong(1);
                    model.setId(id);

                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Reservation find(Long id) {

        Reservation reservation = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    reservation = Reservation.builder()
                                             .id(rs.getLong(1))
                                             .createdAt(rs.getTimestamp(2))
                                             .status(rs.getString(3))
                                             .build();

                }else{
                    throw new IllegalArgumentException("Reservation not found");
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;

    }

    @Override
    public void delete(Long id) {

        try(PreparedStatement ps = this.conn.prepareStatement(DELETE_QUERY)){

            ps.setLong(1, id);
            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Reservation model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setTimestamp(1, model.getCreatedAt());
            ps.setString(2, model.getStatus());
            ps.setLong(3, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Reservation findByTimestamp(Timestamp timestamp) {

        Reservation reservation = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_TIMESTAMP)){

            ps.setTimestamp(1, timestamp);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    reservation = Reservation.builder()
                                             .id(rs.getLong(1))
                                             .createdAt(rs.getTimestamp(2))
                                             .status(rs.getString(3))
                                             .build();

                }else{
                    throw new IllegalArgumentException("Reservation not found");
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;

    }
}
