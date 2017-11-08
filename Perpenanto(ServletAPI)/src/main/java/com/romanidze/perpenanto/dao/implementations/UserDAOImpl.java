package com.romanidze.perpenanto.dao.implementations;

import com.romanidze.perpenanto.dao.interfaces.UserDAOInterface;
import com.romanidze.perpenanto.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAOInterface{

    private Connection conn;

    private static final String INSERT_QUERY = "INSERT INTO \"user\" (username_or_email, password) VALUES (?, ?)";
    private static final String FIND_QUERY = "SELECT FROM \"user\" WHERE \"user\".id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM \"user\" ";
    private static final String DELETE_QUERY = "DELETE FROM \"user\" WHERE \"user\".id = ?";
    private static final String UPDATE_QUERY = "UPDATE \"user\" SET(username_or_email, password) = (?, ?) " +
                                                "WHERE \"user\".id = ?";
    private static final String FIND_BY_USERNAME_QUERY = "SELECT FROM \"user\" WHERE \"user\".username_or_email = ?";

    private UserDAOImpl(){}

    public UserDAOImpl(Connection conn){

        this.conn = conn;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<User> findAll() {

        List<User> resultList = new ArrayList<>();

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()){

                User user = User.builder()
                                .id(rs.getLong("id"))
                                .emailOrUsername(rs.getString("username_or_email"))
                                .password(rs.getString("password"))
                                .build();

                resultList.add(user);

            }

        }catch(SQLException e){

            e.printStackTrace();

        }

        return resultList;

    }

    @Override
    public void save(User model) {

        try(PreparedStatement ps = this.conn.prepareStatement(INSERT_QUERY, new String[] {"id"})){

            ps.setString(1, model.getEmailOrUsername());
            ps.setString(2, model.getPassword());
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
    public User find(Long id) {

        User user = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_QUERY)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    user = User.builder()
                               .id(rs.getLong("id"))
                               .emailOrUsername(rs.getString("username_or_email"))
                               .password(rs.getString("password"))
                               .build();

                }else{
                    throw new IllegalArgumentException("User not found");
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

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
    public void update(User model) {

        try(PreparedStatement ps = this.conn.prepareStatement(UPDATE_QUERY)){

            ps.setString(1, model.getEmailOrUsername());
            ps.setString(2, model.getPassword());
            ps.setLong(3, model.getId());

            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User findByUsername(String username) {

        User user = null;

        try(PreparedStatement ps = this.conn.prepareStatement(FIND_BY_USERNAME_QUERY)){

            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    user = User.builder()
                            .id(rs.getLong("id"))
                            .emailOrUsername(rs.getString("username_or_email"))
                            .password(rs.getString("password"))
                            .build();

                }else{
                    throw new IllegalArgumentException("User not found");
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }

}
