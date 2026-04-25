package com.bastug.novashop.product.repository;

import com.bastug.novashop.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
