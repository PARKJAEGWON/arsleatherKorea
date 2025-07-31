package com.groo.kmw.domain.admin.product.repository;

import com.groo.kmw.domain.admin.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long productId);
    
    // ORDER BY RAND() 모든 상품을 섞음 LIMIT :count 원하는 갯수 설정
    @Query(value = "SELECT * FROM product ORDER BY RAND() LIMIT :count",nativeQuery = true)
    List<Product> findRandomProducts(@Param("count") int count);

    @Query(value = "SELECT * FROM product WHERE product_is_seasonal = true ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Product> findSeasonalProducts(@Param("count") int count);

    @Query(value = "SELECT * FROM product WHERE product_gender = :gender ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Product> findByGender(@Param("gender") int gender, @Param("count") int count);

    @Query("SELECT p FROM Product p WHERE p.productBrand = :brand")
    Page<Product> findByProductBrand(@Param("brand")int brand, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productCategory = :category")
    Page<Product> findByProductCategory(@Param("category") int category, Pageable pageable);
}
