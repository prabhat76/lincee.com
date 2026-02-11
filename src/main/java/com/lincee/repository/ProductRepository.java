package com.lincee.repository;

import com.lincee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByBrand(String brand, Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
    List<Product> findByFeaturedTrue();
    Long countByActiveTrue();
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchProducts(String keyword, Pageable pageable);
}