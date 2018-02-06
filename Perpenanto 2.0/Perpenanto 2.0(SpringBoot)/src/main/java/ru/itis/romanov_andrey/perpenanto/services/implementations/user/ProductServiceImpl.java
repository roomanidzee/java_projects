package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

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

    @Autowired
    private ProductRepository productRepository;

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
        List<Product> sortedProducts = new ArrayList<>();

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

        sortedProducts.addAll(compareAttributes.sortList(currentProducts, functionMap, cookieValue));
        return sortedProducts;

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
}
