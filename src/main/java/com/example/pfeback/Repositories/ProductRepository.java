package com.example.pfeback.Repositories;

import com.example.pfeback.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);

    List<Product> findByOfferId(Long offerId);
}
