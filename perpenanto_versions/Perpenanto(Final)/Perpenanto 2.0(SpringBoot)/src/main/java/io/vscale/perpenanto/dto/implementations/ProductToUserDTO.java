package io.vscale.perpenanto.dto.implementations;

import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.usermodels.ProductToUser;
import io.vscale.perpenanto.models.transfermodels.ProductToUserTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * 01.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ProductToUserDTO implements EntityDTOInterface<ProductToUserTransfer, ProductToUser>{

    @Override
    public List<ProductToUserTransfer> convert(List<ProductToUser> originalList) {

        List<ProductToUserTransfer> resultList = new ArrayList<>();

        originalList.forEach(productToUser ->
                        productToUser.getProducts()
                                     .forEach(product ->
                                               resultList.add(ProductToUserTransfer.builder()
                                                                                   .userId(productToUser.getUser().getId())
                                                                                   .productId(product.getId())
                                                                                   .build())));

        return resultList;

    }
}
