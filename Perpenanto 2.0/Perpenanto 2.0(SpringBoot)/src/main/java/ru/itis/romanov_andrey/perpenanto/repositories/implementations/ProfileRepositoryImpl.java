package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ProfileRepositoryImpl implements ProfileRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, Profile> profiles = new HashMap<>();

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM profile LEFT JOIN \"user\" ON \"user\".id = profile.user_id";
    private static final String INSERT_QUERY =
            "INSERT INTO profile(user_id, email, name, surname, phone) VALUES(?, ?, ?, ?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM profile LEFT JOIN \"user\" ON \"user\".id = profile.user_id " +
                    "WHERE profile.id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM profile WHERE profile.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE profile SET(user_id, email, name, surname, phone) = (?, ?, ?, ?, ?) " +
                    "WHERE profile.id = ?";
    private static final String FIND_BY_USER_QUERY =
            "SELECT * FROM profile LEFT JOIN \"user\" ON \"user\".id = profile.user_id " +
                    "WHERE profile.user_id = ?";

    private RowMapper<Profile> profileRowMapper = (resultSet, rowNumber) -> {

        Long currentProfileId = resultSet.getLong(1);

        if(this.profiles.get(currentProfileId) == null){

            this.profiles.put(currentProfileId, Profile.builder()
                                                       .id(currentProfileId)
                                                       .user(new User())
                                                       .email(resultSet.getString(3))
                                                       .name(resultSet.getString(4))
                                                       .surname(resultSet.getString(5))
                                                       .phone(resultSet.getString(6))
                                                       .build());

        }

        if(resultSet.getLong(7) != 0){

            String roleString = resultSet.getString(11);
            String userStateString = resultSet.getString(12);

            Role[] roles = Role.values();
            UserState[] userStates = UserState.values();

            Optional<Role> role = Arrays.stream(roles)
                                        .filter(role1 -> role1.toString().equals(roleString))
                                        .findAny();

            Optional<UserState> userState = Arrays.stream(userStates)
                                                  .filter(userState1 -> userState1.toString().equals(userStateString))
                                                  .findAny();

            if(!role.isPresent()){
                throw new NullPointerException("role not found!");
            }

            if(!userState.isPresent()){
                throw new NullPointerException("user state not found!");
            }

            User user = User.builder()
                            .login(resultSet.getString(8))
                            .password(resultSet.getString(9))
                            .tempPassword(resultSet.getString(10))
                            .role(role.get())
                            .userState(userState.get())
                            .confirmHash(resultSet.getString(13))
                            .build();

            this.profiles.get(currentProfileId).setUser(user);
        }

        return this.profiles.get(currentProfileId);

    };

    @Override
    public List<Profile> findAll() {

        this.template.query(FIND_ALL_QUERY, this.profileRowMapper);

        List<Profile> result = Lists.newArrayList(this.profiles.values());
        this.profiles.clear();

        return result;

    }

    @Override
    public void save(Profile model) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.template.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
            ps.setLong(1, model.getUser().getId());
            ps.setString(2, model.getEmail());
            ps.setString(3, model.getName());
            ps.setString(4, model.getSurname());
            ps.setString(5, model.getPhone());
            return ps;

        }, keyHolder);
        model.setId(keyHolder.getKey().longValue());

    }

    @Override
    public Profile find(Long id) {

        Profile profile = this.template.queryForObject(FIND_QUERY, this.profileRowMapper, id);
        this.profiles.clear();

        return profile;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(Profile model) {
        this.template.update(UPDATE_QUERY, model.getUser().getId(), model.getEmail(), model.getName(),
                                           model.getSurname(), model.getPhone(), model.getId());
    }

    @Override
    public Profile findByUser(User user) {
        Long id = user.getId();
        Profile profile = this.template.queryForObject(FIND_BY_USER_QUERY, this.profileRowMapper, id);
        this.profiles.clear();

        return profile;
    }
}
