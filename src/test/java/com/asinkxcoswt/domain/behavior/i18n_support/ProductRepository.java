package com.asinkxcoswt.domain.behavior.i18n_support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    default Product create(String name, String description, BigDecimal price) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        return p;
    }
}