package io.vscale.perpenanto.services.implementations.user;

import io.vscale.perpenanto.utils.pagination.Page;
import io.vscale.perpenanto.utils.pagination.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
import io.vscale.perpenanto.services.interfaces.user.ProductService;
import io.vscale.perpenanto.utils.userutils.CompareAttributes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 02.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private CompareAttributes<Product> compareAttributes = (oldList, functionMap, sortType) -> {

        List<Product> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                List<Product> tempList = this.productRepository.findAll();
                resultList.addAll(tempList);
                break;

            case "id":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("id")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "title":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("title")))
                                    .collect(Collectors.toList());
                break;

            case "price":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("price")))
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<Product> getProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCookie(String cookieValue) {

        List<Product> currentProducts = this.productRepository.findAll();

        int size = 3;

        Function<Product, String> idFunction = product -> String.valueOf(product.getId());
        Function<Product, String> titleFunction = Product::getTitle;
        Function<Product, String> priceFunction = product -> String.valueOf(product.getPrice());

        List<Function<Product, String>> functions = Arrays.asList(idFunction, titleFunction, priceFunction);
        List<String> sortTypes = Arrays.asList("id", "title", "price");

        Map<String, Function<Product, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEach(i ->{

            String sortType = sortTypes.get(i);
            Function<Product, String> function = functions.get(i);
            functionMap.put(sortType, function);

        });

        return new ArrayList<>(compareAttributes.sortList(currentProducts, functionMap, cookieValue));

    }

    @Override
    public List<Product> getRandomProducts() {

        Random rand = new Random();
        List<Product> allProducts = this.productRepository.findAll();
        int min = 0;
        int max = allProducts.size() - 1;

        return IntStream.range(0, 3)
                        .mapToObj(i -> allProducts.get(rand.nextInt((max - min) + 1) + min))
                        .collect(Collectors.toList());

    }

    @Override
    public void saveProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        this.productRepository.update(product);
    }

    @Override
    public void deleteProduct(Product product) {
        this.productRepository.delete(product.getId());
    }

    @Override
    public List<Product> getProductsByQuery(String query) {
        return this.productRepository.findByUserQuery(query);
    }

    @Override
    public List<Product> getProductsByRange(Integer start, Integer end) {
        return this.productRepository.findByPriceRange(start, end);
    }

    @Override
    public Page<Product> getProductsByPage(PageRequest pageRequest) {
        return this.productRepository.findAll(pageRequest);
    }

    @Override
    public Long countProducts() {

        Long limit = (long)6;

        return this.productRepository.countAllProducts() / limit;
    }

    @Override
    public Long getMaxPrice() {
        return this.productRepository.getMaxPrice();
    }
}
