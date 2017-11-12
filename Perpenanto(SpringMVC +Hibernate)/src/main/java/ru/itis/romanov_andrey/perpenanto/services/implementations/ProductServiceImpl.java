package ru.itis.romanov_andrey.perpenanto.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.ProductDAOInterface;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.UserDAOInterface;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.ProductToUserTransferImpl;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.ProductToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.Product;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ProductToUser;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.ProductServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;
import ru.itis.romanov_andrey.perpenanto.utils.StreamCompareAttributes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductServiceImpl implements ProductServiceInterface {

    @Autowired
    private ProductDAOInterface productDAO;

    @Autowired
    private UserDAOInterface userDAO;

    @Override
    public Product convertToProduct(ProductToUser productToUser) {
        return this.productDAO.find(productToUser.getProductId());
    }

    @Override
    public List<Product> getProducts() {
        return this.productDAO.findAll();
    }

    @Override
    public List<Product> getProductsByCookie(String cookieValue) {

        List<Product> currentProducts = this.productDAO.findAll();
        List<Product> sortedProducts = new ArrayList<>();

        int size = 3;

        Function<Product, String> zero = (Product p) -> String.valueOf(p.getId());
        Function<Product, String> first = Product::getTitle;
        Function<Product, String> second = (Product p) -> String.valueOf(p.getPrice());

        List<Function<Product, String>> functions = Arrays.asList(zero, first, second);
        List<String> indexes = Arrays.asList("0", "1", "2");

        Map<String, Function<Product, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<Product> compareAttr = new StreamCompareAttributes<>();

        sortedProducts.addAll(compareAttr.sortList(currentProducts, functionMap, cookieValue));

        return sortedProducts;

    }

    @Override
    public List<ProductToUser> getProductsToUser() {

        ProductToUserTransferInterface productToUserDTO = new ProductToUserTransferImpl();
        return productToUserDTO.getProductsToUsers(this.userDAO.findAll());

    }

    @Override
    public List<ProductToUser> getProductsToUserByCookie(String cookieValue) {

        ProductToUserTransferInterface productToUserDTO = new ProductToUserTransferImpl();

        List<ProductToUser> currentProductToUsers = productToUserDTO.getProductsToUsers(this.userDAO.findAll());
        List<ProductToUser> sortedList = new ArrayList<>();

        int size = 3;

        Function<ProductToUser, String> first = (ProductToUser tpi) -> String.valueOf(tpi.getUserId());
        Function<ProductToUser, String> second = (ProductToUser tpi) -> String.valueOf(tpi.getProductId());

        List<Function<ProductToUser, String>> functions = Arrays.asList( first, second);
        List<String> indexes = Arrays.asList( "1", "2");

        Map<String, Function<ProductToUser, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEachOrdered(i -> functionMap.put(indexes.get(i), functions.get(i)));

        CompareAttributes<ProductToUser> compareAttr = new StreamCompareAttributes<>();
        sortedList.addAll(compareAttr.sortList(currentProductToUsers, functionMap, cookieValue));

        return sortedList;

    }

    @Override
    public void saveOrUpdate(Product product) {
        this.productDAO.save(product);
    }

    @Override
    public void delete(Long id) {
        this.productDAO.delete(id);
    }
}
