package com.romanidze.perpenanto.dto.implementations;

import com.romanidze.perpenanto.dto.interfaces.ProductToUserTransferInterface;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.models.ProductToUser;
import com.romanidze.perpenanto.models.temp.TempProductToUser;
import com.romanidze.perpenanto.utils.Increase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductToUserTransferImpl implements ProductToUserTransferInterface{

    private Increase mathAction = new Increase();

    @Override
    public List<TempProductToUser> getTempProductToUsers(List<ProductToUser> oldList) {

        long count = 0;
        int listSize = oldList.size();
        int countSize = oldList.stream()
                               .mapToInt(productToUser -> productToUser.getProducts().size())
                               .sum();

        List<Long> ids = IntStream.range(0, countSize)
                                  .mapToObj(i -> this.mathAction.incrementLong(count))
                                  .collect(Collectors.toList());

        List<Long> userIds = oldList.stream()
                                    .map(ProductToUser::getId)
                                    .collect(Collectors.toList());

        Map<Long, List<Product>> productMap = new HashMap<>();

        for(int i = 0; i < listSize; i++){
            productMap.put(userIds.get(i), oldList.get(i).getProducts());
        }

        List<TempProductToUser> resultList = IntStream.range(0, countSize)
                                                      .mapToObj(
                                                              i -> new TempProductToUser(ids.get(i), (long) 0, (long) 0)
                                                      )
                                                      .collect(Collectors.toList());

        int count1 = -1;
        int count2 = -1;

        productMap.forEach(
                (userId, products) ->
                        IntStream.range(0, products.size())
                                 .forEachOrdered(i -> {
                                                       resultList.get(this.mathAction.incrementInt(count1)).setUserId(userId);
                                                       resultList.get(this.mathAction.incrementInt(count2))
                                                                 .setProductId(products.get(i).getId());
                                                      }));

        return resultList;

    }
}
