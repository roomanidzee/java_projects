package com.romanidze.perpenanto.dao.implementations;

import com.google.common.collect.Lists;
import com.romanidze.perpenanto.dao.interfaces.ProductDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ProfileDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ReservationDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ReservationInfoDAOInterface;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.ReservationInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ReservationInfoDAOImpl implements ReservationInfoDAOInterface{

    private Connection conn;
    private ProfileDAOInterface profileDAO;
    private ReservationDAOInterface reservationDAO;
    private ProductDAOInterface productDAO;

    private static final String INSERT_QUERY =
            "INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES(?, ?, ?)";
    private static final String FIND_QUERY = "SELECT FROM reservation_info WHERE reservation_info.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM reservation_info";
    private static final String DELETE_QUERY = "DELETE FROM reservation_info WHERE reservation_info.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE reservation_info SET(user_id, reservation_id, reservation_product_id) = (?, ?, ?) " +
                    "WHERE reservation_info.id = ?";
    private static final String FIND_BY_USER_QUERY = "SELECT * FROM reservation_info " +
                                                      "WHERE reservation_info.user_id = ?";
    private static final String FIND_BY_RESERVATION_QUERY = "SELECT * FROM reservation_info " +
                                                         "WHERE reservation_info.reservation_id = ?";

    private ReservationInfoDAOImpl(){}

    public ReservationInfoDAOImpl(Connection conn){

        this.conn = conn;
        this.profileDAO = new ProfileDAOImpl(conn);
        this.reservationDAO = new ReservationDAOImpl(conn);
        this.productDAO = new ProductDAOImpl(conn);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ReservationInfo> findAll() {

        List<ReservationInfo> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                ReservationInfo reservationInfo = ReservationInfo.builder()
                                                                 .id(rs.getLong(1))
                                                                 .userProfile(this.profileDAO.find(rs.getLong(2)))
                                                                 .userReservation(this.reservationDAO.find(
                                                                                    rs.getLong(3))
                                                                                 )
                                                                 .reservationProducts(Lists.newArrayList())
                                                                 .build();
                reservationInfo.getReservationProducts().add(this.productDAO.find(rs.getLong(4)));
                resultList.add(reservationInfo);

            }

        }catch (SQLException e) {

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(ReservationInfo model) {

        int count = model.getReservationProducts().size();
        List<Product> products = model.getReservationProducts();

        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[]{"id"})){

            IntStream.range(0, count).forEachOrdered(i -> {

                try{

                   ps.setLong(1, model.getUserProfile().getId());
                   ps.setLong(2, model.getUserReservation().getId());
                   ps.setLong(3, products.get(i).getId());
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
    public ReservationInfo find(Long id) {

        ReservationInfo reservationInfo = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                reservationInfo = ReservationInfo.builder()
                                                 .id(rs.getLong(1))
                                                 .userProfile(this.profileDAO.find(rs.getLong(2)))
                                                 .userReservation(this.reservationDAO.find(
                                                         rs.getLong(3))
                                                 )
                                                 .reservationProducts(Lists.newArrayList())
                                                 .build();
                reservationInfo.getReservationProducts().add(this.productDAO.find(rs.getLong(4)));

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationInfo;

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
    public void update(ReservationInfo model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setLong(1, model.getUserProfile().getId());
            ps.setLong(2, model.getUserReservation().getId());
            ps.setLong(3, model.getReservationProducts().get(0).getId());
            ps.setLong(4, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ReservationInfo> findAllByUserId(Long userId) {

        List<ReservationInfo> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_USER_QUERY)){

            ps.setLong(1, userId);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){

                    ReservationInfo reservationInfo = ReservationInfo.builder()
                                                                     .id(rs.getLong(1))
                                                                     .userProfile(this.profileDAO.find(rs.getLong(2)))
                                                                     .userReservation(this.reservationDAO.find(
                                                                             rs.getLong(3))
                                                                     )
                                                                     .reservationProducts(Lists.newArrayList())
                                                                     .build();
                    reservationInfo.getReservationProducts().add(this.productDAO.find(rs.getLong(4)));
                    resultList.add(reservationInfo);


                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }

    @Override
    public List<ReservationInfo> findAllByReservationId(Long reservationId) {
        List<ReservationInfo> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_RESERVATION_QUERY)){

            ps.setLong(1, reservationId);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){

                    ReservationInfo reservationInfo = ReservationInfo.builder()
                                                                     .id(rs.getLong(1))
                                                                     .userProfile(this.profileDAO.find(rs.getLong(2)))
                                                                     .userReservation(this.reservationDAO.find(
                                                                             rs.getLong(3))
                                                                     )
                                                                     .reservationProducts(Lists.newArrayList())
                                                                     .build();
                    reservationInfo.getReservationProducts().add(this.productDAO.find(rs.getLong(4)));
                    resultList.add(reservationInfo);


                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
