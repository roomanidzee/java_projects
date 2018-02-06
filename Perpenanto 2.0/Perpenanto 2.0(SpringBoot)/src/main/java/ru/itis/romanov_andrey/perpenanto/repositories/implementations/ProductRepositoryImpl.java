package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;

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
public class ProductRepositoryImpl implements ProductRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, Product> products = new HashMap<>();

    private static final String FIND_ALL_QUERY = "SELECT * FROM product";
    private static final String INSERT_QUERY =
            "INSERT INTO product(title, price, description, photo_link) VALUES (?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT * FROM product WHERE product.id = ?";
    private static final String DELETE_QUERY = "DELETE FROM product WHERE product.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE product SET(title, price, description, photo_link) = (?, ?, ?, ?) WHERE product.id = ?";
    private static final String FIND_BY_TITLE_QUERY =
            "SELECT * FROM product WHERE product.title = ?";

    private RowMapper<Product> productRowMapper = (resultSet, rowNumber) -> {

        Long currentProductId = resultSet.getLong(1);

        if(this.products.get(currentProductId) == null){

            this.products.put(currentProductId, Product.builder()
                                                       .id(currentProductId)
                                                       .title(resultSet.getString(2))
                                                       .price(resultSet.getInt(3))
                                                       .description(resultSet.getString(4))
                                                       .photoLink(resultSet.getString(5))
                                                       .build());

        }

        return this.products.get(currentProductId);

    };

    @Override
    public List<Product> findAll() {

        this.template.query(FIND_ALL_QUERY, this.productRowMapper);

        List<Product> result = Lists.newArrayList(this.products.values());
        this.products.clear();

        return result;

    }

    @Override
    public void save(Product model) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.template.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
            ps.setString(1, model.getTitle());
            ps.setInt(2, model.getPrice());
            ps.setString(3, model.getDescription());
            ps.setString(4, model.getPhotoLink());
            return ps;

        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());

    }

    @Override
    public Product find(Long id) {

        Product product = this.template.queryForObject(FIND_QUERY, this.productRowMapper, id);
        this.products.clear();

        return product;
    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(Product model) {
        this.template.update(UPDATE_QUERY, model.getTitle(), model.getPrice(), model.getDescription(),
                                           model.getPhotoLink(), model.getId());
    }

    @Override
    public Product findByTitle(String title) {

        Product product = this.template.queryForObject(FIND_BY_TITLE_QUERY, this.productRowMapper, title);
        this.products.clear();

        return product;
    }
}
