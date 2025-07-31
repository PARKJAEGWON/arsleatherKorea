package com.groo.kmw.domain.admin.product.entity;

import com.groo.kmw.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    private String productCode;

    private String productName;

    private int productBrand;   // 0. 언브로운

    private int productCategory; // 0.브리프케이스 1.도트백, 2.백팩, 3.핸드백

    private String productMaterial;

    private String productColor;

//    private int productStock;

    private int productPrice;

    private String productDescription;

    private int productStatus = 0; //0 판매가능 8 숨기기 9 품절

    private String mainImageUrl;

    private String detailImageUrls;

    private int productGender; // 상품의 사용자 0 공용 1 남자 2 여자

    private boolean productIsSeasonal;

}