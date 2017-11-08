package com.romanidze.perpenanto.dao.implementations;

import com.google.common.collect.Lists;
import com.romanidze.perpenanto.dao.interfaces.ProductDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.ProductToUserDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.UserDAOInterface;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.ProductToUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ProductToUserDAOImpl implements ProductToUserDAOInterface{

    private Connection conn;
    private UserDAOInterface userDAO;
    private ProductDAOInterface productDAO;

    private static final String INSERT_QUERY = "INSERT INTO product_to_user(user_id, product_id) VALUES (?, ?)";
    private static final String FIND_QUERY = "SELECT FROM product_to_user WHERE product_to_user.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM product_to_user " +
            "LEFT JOIN \"user\" ON \"user\".id = product_to_user.user_id " +
            "LEFT JOIN product ON product.id = product_to_user.product_id";
    private static final String DELETE_QUERY = "DELETE FROM product_to_user WHERE product_to_user.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE product_to_user SET(user_id, product_id) = (?, ?) WHERE product_to_user.id = ?";
    private static final String FIND_BY_USER_QUERY = "SELECT * FROM product_to_user WHERE product_to_user.user_id = ?";

    private ProductToUserDAOImpl(){}

    public ProductToUserDAOImpl(Connection conn){

        this.conn = conn;
        this.userDAO = new UserDAOImpl(conn);
        this.productDAO = new ProductDAOImpl(conn);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ProductToUser> findAll() {

        List<ProductToUser> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                ProductToUser productToUser = ProductToUser.builder()
                                                           .id(rs.getLong(1))
                                                           .user(this.userDAO.find(rs.getLong(2)))
                                                           .products(Lists.newArrayList())
                                                           .build();

                productToUser.getProducts().add(this.productDAO.find(rs.getLong(3)));
                resultList.add(productToUser);

            }

        }catch (SQLException e) {

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(ProductToUser model) {

        int count = model.getProducts().size();
        List<Product> products = model.getProducts();

        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[]{"id"})){

            IntStream.range(0, count).forEachOrdered(i ->{

                try{

                    ps.setLong(1, model.getUser().getId());
                    ps.setLong(2, products.get(i).getId());
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
    public ProductToUser find(Long id) {

        ProductToUser productToUser = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                productToUser = ProductToUser.builder()
                                             .id(rs.getLong(1))
                                             .user(this.userDAO.find(rs.getLong(2)))
                                             .products(Lists.newArrayList())
                                             .build();

                productToUser.getProducts().add(this.productDAO.find(rs.getLong(3)));

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return productToUser;

    }

    @Override
    public void delete(Long id) {

        try(PreparedStatement ps = this.conn.prepareStatement(DELETE_QUERY)){

            ps.setLong(1, id);
            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        this.productDAO.delete(id);

    }

    @Override
    public void update(ProductToUser model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setLong(1, model.getUser().getId());
            ps.setLong(2, model.getProducts().get(0).getId());
            ps.setLong(3, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ProductToUser> findAllByUser(Long userId) {

        List<ProductToUser> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_USER_QUERY)){

            ps.setLong(1, userId);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){

                    ProductToUser productToUser = ProductToUser.builder()
                                                               .id(rs.getLong(1))
                                                               .user(this.userDAO.find(rs.getLong(2)))
                                                               .products(Lists.newArrayList())
                                                               .build();
                    productToUser.getProducts().add(this.productDAO.find(rs.getLong(3)));
                    resultList.add(productToUser);


                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }
}
