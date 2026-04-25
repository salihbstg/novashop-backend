package com.bastug.novashop.repository;

import com.bastug.novashop.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
