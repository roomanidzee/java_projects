package io.vscale.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.vscale.perpenanto.models.usermodels.Address;
import io.vscale.perpenanto.repositories.interfaces.AddressRepository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class AddressRepositoryImpl implements AddressRepository{

    private final JdbcTemplate template;
    private Map<Long, Address> addresses = new HashMap<>();

    @Autowired
    public AddressRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM address";
    private static final String INSERT_QUERY =
            "INSERT INTO address(country, postal_code, city, street, home_number) VALUES(?, ?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT * FROM address WHERE address.id = ?";
    private static final String DELETE_QUERY = "DELETE FROM address WHERE address.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE address SET(country, postal_code, city, street, home_number) = (?, ?, ?, ?, ?) " +
                    "WHERE address.id = ?";
    private static final String FIND_BY_POSTAL_CODE = "SELECT * FROM address WHERE address.postal_code = ?";

    private RowMapper<Address> addressRowMapper = (resultSet, rowNumber) ->{

        Long currentAddressId = resultSet.getLong(1);

        if(this.addresses.get(currentAddressId) == null){

            this.addresses.put(currentAddressId, Address.builder()
                                                        .id(currentAddressId)
                                                        .country(resultSet.getString(2))
                                                        .postalCode(resultSet.getInt(3))
                                                        .city(resultSet.getString(4))
                                                        .street(resultSet.getString(5))
                                                        .homeNumber(resultSet.getInt(6))
                                                        .build());

        }

        return this.addresses.get(currentAddressId);

    };

    @Override
    public List<Address> findAll() {

        this.template.query(FIND_ALL_QUERY, this.addressRowMapper);

        List<Address> result = Lists.newArrayList(this.addresses.values());
        this.addresses.clear();

        return result;

    }

    @Override
    public void save(Address model) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.template.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
            ps.setString(1, model.getCountry());
            ps.setInt(2, model.getPostalCode());
            ps.setString(3, model.getCity());
            ps.setString(4, model.getStreet());
            ps.setInt(5, model.getHomeNumber());
            return ps;

        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());

    }

    @Override
    public Address find(Long id) {

        Address address = this.template.queryForObject(FIND_QUERY, this.addressRowMapper, id);
        this.addresses.clear();

        return address;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(Address model) {
        this.template.update(UPDATE_QUERY, model.getCountry(), model.getPostalCode(), model.getCity(),
                                           model.getStreet(), model.getHomeNumber(), model.getId());
    }

    @Override
    public Address findByPostalCode(Integer postalCode) {

        Address address = this.template.queryForObject(FIND_BY_POSTAL_CODE, this.addressRowMapper, postalCode);
        this.addresses.clear();

        return address;

    }
}
