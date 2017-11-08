package com.romanidze.perpenanto.dao.implementations;

import com.google.common.collect.Lists;
import com.romanidze.perpenanto.dao.interfaces.BusketDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ProductDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ProfileDAOInterface;
import com.romanidze.perpenanto.models.Busket;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BusketDAOImpl implements BusketDAOInterface {

    private Connection conn;
    private ProfileDAOInterface profileDAO;
    private ProductDAOInterface productDAO;

    private static final String INSERT_QUERY = "INSERT INTO busket(user_id, reservation_product_id) VALUES(?, ?)";
    private static final String FIND_QUERY = "SELECT FROM busket WHERE busket.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM busket";
    private static final String DELETE_QUERY = "DELETE FROM busket WHERE busket.id = ?";
    private static final String UPDATE_QUERY = "UPDATE busket SET(user_id, reservation_product_id) = (?, ?) " +
            "WHERE busket.id = ?";
    private static final String DELETE_BY_USER_QUERY = "DELETE FROM busket WHERE busket.user_id = ?";
    private static final String FIND_BY_USER_QUERY = "SELECT * FROM busket WHERE busket.user_id = ?";
    //запросы для оплаты заказа
    private static final String PAY_QUERY1 =
            "INSERT INTO reservation_info(user_id, reservation_id, reservation_product_id) VALUES(?, ?, ?)";
    private static final String PAY_QUERY2 =
            "INSERT INTO reservation_to_user(user_id, user_reservation_id) VALUES(?, ?)";

    private BusketDAOImpl() {}

    public BusketDAOImpl(Connection conn) {

        this.conn = conn;
        this.profileDAO = new ProfileDAOImpl(conn);
        this.productDAO = new ProductDAOImpl(conn);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Busket> findAll() {

        List<Busket> resultList = new ArrayList<>();

        try (PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Busket busket = Busket.builder()
                                      .id(rs.getLong(1))
                                      .userProfile(this.profileDAO.find(rs.getLong(2)))
                                      .products(Lists.newArrayList())
                                      .build();

                busket.getProducts().add(this.productDAO.find(rs.getLong(3)));

                resultList.add(busket);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(Busket model) {

        int count = model.getProducts().size();
        List<Product> products = model.getProducts();

        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[]{"id"})) {

            IntStream.range(0, count).forEachOrdered(i -> {
                try {
                    ps.setLong(1, model.getUserProfile().getId());
                    ps.setLong(2, products.get(i).getId());
                    ps.addBatch();
                } catch (SQLException e) {
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

        } catch (SQLException e) {
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
    public Busket find(Long id) {

        Busket busket = null;

        try (PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                busket = Busket.builder()
                        .id(rs.getLong(1))
                        .userProfile(this.profileDAO.find(rs.getLong(2)))
                        .products(Lists.newArrayList())
                        .build();

                busket.getProducts().add(this.productDAO.find(rs.getLong(3)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return busket;

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
    public void update(Busket model) {

        if(model.getProducts().size() > 1){

            deleteByUser(model.getUserProfile().getId());
            save(model);

        }else{

            try (PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)) {

                ps.setLong(1, model.getUserProfile().getId());
                ps.setLong(2, model.getProducts().get(0).getId());
                ps.setLong(3, model.getId());

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void payForBusket(Busket model, Reservation reservation) {

        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement ps = this.conn.prepareStatement(PAY_QUERY1, new String[]{"id"})) {

            int count = model.getProducts().size();
            List<Product> products = model.getProducts();

            IntStream.range(0, count).forEachOrdered(i -> {
                try {
                    ps.setLong(1, model.getUserProfile().getId());
                    ps.setLong(2, reservation.getId());
                    ps.setLong(3, products.get(i).getId());
                    ps.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            ps.executeBatch();

            this.conn.commit();

        } catch (SQLException e) {
            try {
                this.conn.rollback();
                this.conn.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        try (PreparedStatement ps = this.conn.prepareStatement(PAY_QUERY2, new String[]{"id"})) {

            this.conn.setAutoCommit(true);

            ps.setLong(1, model.getUserProfile().getUserId());
            ps.setLong(2, reservation.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteByUser(Long userId) {

        try(PreparedStatement ps = this.conn.prepareStatement(DELETE_BY_USER_QUERY)){

            ps.setLong(1, userId);
            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Busket findByUser(Long userId) {

        Busket busket = Busket.builder()
                              .userProfile(this.profileDAO.find(userId))
                              .products(Lists.newArrayList())
                              .build();


        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_USER_QUERY);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                busket.getProducts().add(this.productDAO.find(rs.getLong(3)));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return busket;

    }
}
