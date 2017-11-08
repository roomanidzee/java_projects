package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.ProductToUser;
import com.romanidze.perpenanto.models.temp.TempProductToUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProductToUserServiceInterface {

    List<TempProductToUser> getProductToUserByCookie(HttpServletRequest req, HttpServletResponse resp);
    List<TempProductToUser> getProducts();

    void addProductToUser(ProductToUser productToUser);
    void updateProductToUser(ProductToUser productToUser);
    void deleteProductToUser(Long id);

}
