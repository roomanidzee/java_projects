package com.romanidze.perpenanto.dao.implementations;

import com.romanidze.perpenanto.dao.interfaces.ProfileDAOInterface;
import com.romanidze.perpenanto.dao.interfaces.AddressToUserDAOInterface;
import com.romanidze.perpenanto.models.AddressToUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressToUserDAOImpl implements AddressToUserDAOInterface {

    private Connection conn;
    private ProfileDAOInterface profileDAO;

    private static final String INSERT_QUERY =
            "INSERT INTO address_to_user(user_id, country, post_index, city, street, home_number) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT FROM address_to_user WHERE address_to_user.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM address_to_user LEFT JOIN \"user\" ON \"user\".id = address_to_user.user_id";
    private static final String DELETE_QUERY = "DELETE FROM address_to_user WHERE address_to_user.id = ?";
    private static final String UPDATE_QUERY = "UPDATE address_to_user " +
     "SET (user_id, country, post_index, city, street, home_number) = (?, ?, ?, ?, ?, ?) " +
            "WHERE address_to_user.id = ?";
    private static final String FIND_BY_USER_QUERY = "SELECT FROM address_to_user WHERE address_to_user.user_id = ?";

    private AddressToUserDAOImpl(){}

    public AddressToUserDAOImpl(Connection conn){

        this.conn = conn;
        this.profileDAO = new ProfileDAOImpl(conn);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<AddressToUser> findAll() {

        List<AddressToUser> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                AddressToUser addressToUser = AddressToUser.builder()
                                                     .id(rs.getLong(1))
                                                     .userId(rs.getLong(2))
                                                     .country(rs.getString(3))
                                                     .postalCode(rs.getInt(4))
                                                     .city(rs.getString(5))
                                                     .street(rs.getString(6))
                                                     .homeNumber(rs.getInt(7))
                                                     .build();

                resultList.add(addressToUser);

            }

        }catch(SQLException e){

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(AddressToUser model) {

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[]{"id"})){

            ps.setLong(1, model.getUserId());
            ps.setString(2, model.getCountry());
            ps.setInt(3, model.getPostalCode());
            ps.setString(4, model.getCity());
            ps.setString(5, model.getStreet());
            ps.setInt(6, model.getHomeNumber());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){

                if(rs.next()){

                    Long id = rs.getLong(1);
                    model.setId(id);

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public AddressToUser find(Long id) {

        AddressToUser addressToUser = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    addressToUser = AddressToUser.builder()
                                                 .id(rs.getLong(1))
                                                 .userId(rs.getLong(2))
                                                 .country(rs.getString(3))
                                                 .postalCode(rs.getInt(4))
                                                 .city(rs.getString(5))
                                                 .street(rs.getString(6))
                                                 .homeNumber(rs.getInt(7))
                                                 .build();

                }else{
                    throw new IllegalArgumentException("AddressToUser not found");
                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return addressToUser;
    }

    @Override
    public void delete(Long id) {

        try(PreparedStatement ps = this.conn.prepareStatement(DELETE_QUERY)){

            ps.setLong(1, id);
            ps.executeUpdate();

            this.profileDAO.delete(id);

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(AddressToUser model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setLong(1, model.getUserId());
            ps.setString(2, model.getCountry());
            ps.setInt(3, model.getPostalCode());
            ps.setString(4, model.getCity());
            ps.setString(5, model.getStreet());
            ps.setInt(6, model.getHomeNumber());
            ps.setLong(7, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<AddressToUser> findByUserId(Long userId) {

        List<AddressToUser> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_USER_QUERY)){

            ps.setLong(1, userId);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    resultList.add(AddressToUser.builder()
                                                .id(rs.getLong(1))
                                                .userId(rs.getLong(2))
                                                .country(rs.getString(3))
                                                .postalCode(rs.getInt(4))
                                                .city(rs.getString(5))
                                                .street(rs.getString(6))
                                                .homeNumber(rs.getInt(7))
                                                .build());

                }else{
                    throw new IllegalArgumentException("AddressToUser not found");
                }

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;

    }
}
