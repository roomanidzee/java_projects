package io.vscale.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import io.vscale.perpenanto.utils.pagination.Page;
import io.vscale.perpenanto.utils.pagination.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository{

    private JdbcTemplate template;
    private JdbcTemplateWrapper<Product> jdbcTemplateWrapper;

    @Autowired
    public ProductRepositoryImpl(JdbcTemplate template, JdbcTemplateWrapper<Product> jdbcTemplateWrapper) {
        this.template = template;
        this.jdbcTemplateWrapper = jdbcTemplateWrapper;
    }

    private Map<Long, Product> products = new HashMap<>();

    private static final String FIND_ALL_QUERY = "SELECT * FROM product";
    private static final String INSERT_QUERY =
            "INSERT INTO product(title, price, description, photo_link) VALUES (?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT * FROM product WHERE product.id = ?";
    private static final String DELETE_QUERY = "DELETE FROM product WHERE product.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE product SET(title, price, description, photo_link) = (?, ?, ?, ?) WHERE product.id = ?";
    private static final String FIND_BY_TITLE_QUERY = "SELECT * FROM product WHERE product.title = ?";
    private static final String FIND_BY_USER_TITLE_QUERY ="SELECT * FROM product WHERE product.title ILIKE ? ESCAPE '!'";
    private static final String FIND_BY_PRICE_RANGE_QUERY = "SELECT * FROM product WHERE product.price BETWEEN ? AND ?";
    private static final String PAGINATE_QUERY =  "SELECT * FROM product LIMIT ? OFFSET ?";
    private static final String COUNT_QUERY = "SELECT COUNT(id) FROM product";
    private static final String MAX_PRICE_QUERY = "SELECT MAX(price) FROM product";

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

        Optional<Product> tempProduct =
                this.jdbcTemplateWrapper.findByItem(FIND_BY_TITLE_QUERY, this.productRowMapper, title);

        this.products.clear();

        return tempProduct.orElse(null);

    }

    @Override
    public List<Product> findByUserQuery(String titleQuery) {

        titleQuery = titleQuery.replace("!", "!!")
                               .replace("%", "!%")
                               .replace("_", "!_")
                               .replace("[", "![");

        String finalQuery = titleQuery;

        this.template.query(connection -> {

            PreparedStatement ps = connection.prepareStatement(FIND_BY_USER_TITLE_QUERY);
            ps.setString(1, "%" + finalQuery + "%");
            return ps;

        }, this.productRowMapper);

        List<Product> result = Lists.newArrayList(this.products.values());
        this.products.clear();

        return result;

    }

    @Override
    public List<Product> findByPriceRange(Integer start, Integer end) {

        this.template.query(connection -> {

            PreparedStatement ps = connection.prepareStatement(FIND_BY_PRICE_RANGE_QUERY);
            ps.setInt(1, start);
            ps.setInt(2, end);
            return ps;

        }, this.productRowMapper);

        List<Product> result = Lists.newArrayList(this.products.values());
        this.products.clear();

        return result;

    }

    @Override
    public Page<Product> findAll(PageRequest pageRequest) {

       this.template.query(connection -> {

           PreparedStatement ps = connection.prepareStatement(PAGINATE_QUERY);
           ps.setInt(1, pageRequest.getPageLimit());
           ps.setInt(2, pageRequest.getPageOffset());
           return ps;

       }, this.productRowMapper);

        List<Product> result = Lists.newArrayList(this.products.values());
        this.products.clear();

        return Page.<Product>builder()
                   .pageLimit(pageRequest.getPageLimit())
                   .pageNumber(pageRequest.getPageOffset())
                   .content(result)
                   .build();

    }

    @Override
    public Long countAllProducts() {
        return this.template.queryForObject(COUNT_QUERY, Long.class);
    }

    @Override
    public Long getMaxPrice() {
        return this.template.queryForObject(MAX_PRICE_QUERY, Long.class);
    }
}
