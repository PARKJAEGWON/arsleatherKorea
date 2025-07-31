package com.groo.kmw.domain.admin.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductCreateRequest {

    private String productCode;

    private String productName;

    private int productBrand;

    private int productCategory;

    private String productMaterial;

    private String productColor;

//    private int productStock;

    private int productPrice;

    private String productDescription;

    private MultipartFile mainImage;

    private List<MultipartFile> detailImages;

    private int productGender;

    private boolean productIsSeasonal;
}
