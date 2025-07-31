package com.groo.kmw.domain.admin.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {

    private Long productId;

    private String productName;

    private int productPrice;

    private int productCategory;

    private String productColor;

    private String productMaterial;

    private int productGender;

    private boolean productIsSeasonal;

    private String productDescription;

    private int productStatus;
}
