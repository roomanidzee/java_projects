package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.forms.admin.ProductForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.ProductRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.ProductAdminServiceInterface;

import java.util.Collections;

/**
 * 27.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ProductAdminServiceImpl implements ProductAdminServiceInterface{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void addProduct(ProductForm productForm) {

        User user = this.userRepository.findOne(productForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(productForm.getOrderId());

        Product product = Product.builder()
                                 .title(productForm.getTitle())
                                 .price(productForm.getPrice())
                                 .description(productForm.getDescription())
                                 .photoLink(productForm.getPhotolink())
                                 .users(Collections.singletonList(user))
                                 .reservations(Collections.singletonList(reservation))
                                 .build();

        this.productRepository.save(product);

    }

    @Override
    public void updateProduct(ProductForm productForm) {

        User user = this.userRepository.findOne(productForm.getUserId());
        Reservation reservation = this.reservationRepository.findOne(productForm.getOrderId());
        Product product = this.productRepository.findOne(productForm.getId());

        if(product == null){
            throw new NullPointerException("Product not found!");
        }

        product.setTitle(productForm.getTitle());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setPhotoLink(productForm.getPhotolink());
        product.setUsers(Collections.singletonList(user));
        product.setReservations(Collections.singletonList(reservation));

        this.productRepository.save(product);

    }

    @Override
    public void deleteProduct(Long id) {
        this.productRepository.delete(id);
    }
}
