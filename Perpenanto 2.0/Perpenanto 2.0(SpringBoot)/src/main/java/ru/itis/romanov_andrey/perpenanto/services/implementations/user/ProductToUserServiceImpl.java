package ru.itis.romanov_andrey.perpenanto.services.implementations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.ProductToUserDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.ProductToUserTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.ProductToUser;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProductToUserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductToUserService;
import ru.itis.romanov_andrey.perpenanto.utils.CompareAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 03.02.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductToUserServiceImpl implements ProductToUserService{

    @Autowired
    private ProductToUserRepository productToUserRepository;

    private CompareAttributes<ProductToUserTransfer> compareAttributes = (oldList, functionMap, sortType) -> {

        List<ProductToUserTransfer> resultList = new ArrayList<>();

        switch(sortType){

            case "reset":

                EntityDTOInterface<ProductToUserTransfer, ProductToUser> entityDTO = new ProductToUserDTO();
                resultList.addAll(entityDTO.convert(this.productToUserRepository.findAll()));
                break;

            case "user":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("user")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "product":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("product")).reversed())
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;

    };

    @Override
    public List<ProductToUserTransfer> getProductsToUsers() {
        EntityDTOInterface<ProductToUserTransfer, ProductToUser> entityDTO = new ProductToUserDTO();
        return entityDTO.convert(this.productToUserRepository.findAll());
    }

    @Override
    public List<ProductToUserTransfer> getProductsToUsersByCookie(String cookieValue) {

        EntityDTOInterface<ProductToUserTransfer, ProductToUser> entityDTO = new ProductToUserDTO();
        List<ProductToUserTransfer> currentProductsToUsers = entityDTO.convert(this.productToUserRepository.findAll());
        List<ProductToUserTransfer> sortedProductsToUsers = new ArrayList<>();

        int size = 2;

        Function<ProductToUserTransfer, String> userFunction =
                                            productToUserTransfer -> String.valueOf(productToUserTransfer.getUserId());
        Function<ProductToUserTransfer, String> productFunction =
                                         productToUserTransfer -> String.valueOf(productToUserTransfer.getProductId());

        List<Function<ProductToUserTransfer, String>> functions = Arrays.asList(userFunction, productFunction);
        List<String> sortTypes = Arrays.asList("user", "product");

        Map<String, Function<ProductToUserTransfer, String>> functionMap = new HashMap<>();

        IntStream.range(0, size).forEach(i ->{

            String sortType = sortTypes.get(i);
            Function<ProductToUserTransfer, String> function = functions.get(i);
            functionMap.put(sortType, function);

        });

        sortedProductsToUsers.addAll(this.compareAttributes.sortList(currentProductsToUsers, functionMap, cookieValue));
        return sortedProductsToUsers;

    }

    @Override
    public void saveProductToUser(ProductToUser productToUser) {
       this.productToUserRepository.save(productToUser);
    }

    @Override
    public void updateProductToUser(ProductToUser productToUser) {
       this.productToUserRepository.update(productToUser);
    }

    @Override
    public void deleteProductToUser(Long userId, Long productId) {
       this.productToUserRepository.delete(userId, productId);
    }
}
