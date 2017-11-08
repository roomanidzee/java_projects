package com.romanidze.perpenanto.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Busket {

    private Long id;
    private Profile userProfile;
    private List<Product> products;

    @Override
    public String toString() {

        List<Long> productsIDs = this.products.stream()
                                     .map(Product::getId)
                                     .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();

        sb.append("Busket{")
          .append("id = ").append(this.getId())
          .append(", userProfileID = ").append(this.userProfile.getId())
          .append(", productsIDs = ").append(productsIDs)
          .append("}");

        return sb.toString();

    }
}
