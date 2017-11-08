package com.romanidze.perpenanto.dao.implementations;

import com.romanidze.perpenanto.dao.interfaces.ProductDAOInterface;
import com.romanidze.perpenanto.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAOInterface{

    private Connection conn;

    private static final String INSERT_QUERY =
            "INSERT INTO product(title, price, description, photo_link) VALUES(?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT FROM product WHERE product.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM product";
    private static final String DELETE_QUERY = "DELETE FROM product WHERE product.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE product SET(title, price, description, photo_link) = (?, ?, ?, ?) " +
                    "WHERE product.id = ?";

    private ProductDAOImpl(){}

    public ProductDAOImpl(Connection conn){

        this.conn = conn;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Product> findAll() {

       List<Product> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()){

            Product product = Product.builder()
                                     .id(rs.getLong(1))
                                     .title(rs.getString(2))
                                     .price(rs.getInt(3))
                                     .description(rs.getString(4))
                                     .photoLink(rs.getString(5))
                                     .build();
            resultList.add(product);

        }catch(SQLException e){

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(Product model) {

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[] {"id"})){

            ps.setString(1, model.getTitle());
            ps.setInt(2, model.getPrice());
            ps.setString(3, model.getDescription());
            ps.setString(4, model.getPhotoLink());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){

                if(rs.next()){

                    Long id = rs.getLong(1);
                    model.setId(id);

                }

            }

        }catch(SQLException e){

            e.printStackTrace();

        }

    }

    @Override
    public Product find(Long id) {

        Product product = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    product = Product.builder()
                                     .id(rs.getLong(1))
                                     .title(rs.getString(2))
                                     .price(rs.getInt(3))
                                     .description(rs.getString(4))
                                     .photoLink(rs.getString(5))
                                     .build();

                }else{
                    throw new IllegalArgumentException("Product not found");
                }

            }

        }catch(SQLException e){

            e.printStackTrace();

        }

        return product;

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
    public void update(Product model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setString(1, model.getTitle());
            ps.setInt(2, model.getPrice());
            ps.setString(3, model.getDescription());
            ps.setString(4, model.getPhotoLink());
            ps.setLong(5, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
