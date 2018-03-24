package com.romanidze.perpenanto.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user", "products"})
public class ProductToUser {

    private Long id;
    private User user;
    private List<Product> products;

}
