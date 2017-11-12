package ru.itis.romanov_andrey.perpenanto.dto.implementations;

import ru.itis.romanov_andrey.perpenanto.dto.interfaces.ProductToUserTransferInterface;
import ru.itis.romanov_andrey.perpenanto.models.Product;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.models.adminmodels.ProductToUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ProductToUserTransferImpl implements ProductToUserTransferInterface {
    @Override
    public List<ProductToUser> getProductsToUsers(List<User> users) {

       List<ProductToUser> resultList = new ArrayList<>();

       int listSize = users.size();

        IntStream.range(0, listSize).forEachOrdered(i -> {

            User user = users.get(i);
            Set<Product> products = user.getProducts();

            products.stream()
                    .map(product -> ProductToUser.builder()
                                                 .userId(user.getId())
                                                 .productId(product.getId())
                                                 .build()
                    )
                    .forEachOrdered(resultList::add);

        });

        return resultList;

    }
}
