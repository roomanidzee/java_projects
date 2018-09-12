package io.vscale.perpenanto.dto.implementations;

import io.vscale.perpenanto.dto.interfaces.EntityDTOInterface;
import io.vscale.perpenanto.models.transfermodels.BusketTransfer;
import io.vscale.perpenanto.models.usermodels.Busket;
import io.vscale.perpenanto.models.usermodels.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * 28.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class BusketDTO implements EntityDTOInterface<BusketTransfer, Busket>{

    @Override
    public List<BusketTransfer> convert(List<Busket> originalList) {

        List<BusketTransfer> resultList = new ArrayList<>();

        originalList.forEach(busket -> {

            List<Product> products = busket.getProducts();

            products.stream()
                    .map(product -> BusketTransfer.builder()
                                                  .profileId(busket.getProfile().getId())
                                                  .productId(product.getId())
                                                  .build()
                    )
                    .forEachOrdered(resultList::add);

        });

        return resultList;

    }
}
