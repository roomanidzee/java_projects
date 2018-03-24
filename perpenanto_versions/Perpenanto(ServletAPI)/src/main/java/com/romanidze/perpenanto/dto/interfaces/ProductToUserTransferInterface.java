package com.romanidze.perpenanto.dto.interfaces;

import com.romanidze.perpenanto.models.ProductToUser;
import com.romanidze.perpenanto.models.temp.TempProductToUser;

import java.util.List;

public interface ProductToUserTransferInterface {

    List<TempProductToUser> getTempProductToUsers(List<ProductToUser> oldList);

}
