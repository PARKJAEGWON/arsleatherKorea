package com.groo.kmw.domain.admin.product.service;

import com.groo.kmw.domain.admin.product.entity.Product;
import com.groo.kmw.domain.admin.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${file.upload.path}")
    private String uploadPath;


    //파일 저장
    public String saveFile(MultipartFile multipartFile){
        if(multipartFile == null || multipartFile.isEmpty())
            return null;
        //업로드 된 파일의 원본 이름을 가져옴 (파일 확장자를 추출하거나 로그를 남길때 유용하다함)
        String originalName = multipartFile.getOriginalFilename();
        //충돌 방지를 위해 UUID를 생성함 (여러 사용자가 같은 이름의 파일을 업로드해도 서로 다른이름으로 저장되도록함!)
        String uuid = UUID.randomUUID().toString();
        //원본 파일명에서 확장자 부분만 추출
        String extension = originalName.substring(originalName.lastIndexOf("."));
        // 저장될 파일 이름을 uuid+ 확장자 형태로 생성
        String savedName = uuid + extension;
        //최종 저장 경로 설정
        String savedPath = uploadPath + savedName;
        try{
            multipartFile.transferTo(new File(savedPath));
            //업로드된 파일의 내용을 위에서 만든 경로에 실제로 저장 transferTo는 파일 복사 또는 이동작업을 수행함
            return "/" + savedName;
        }catch (IOException e){
            throw new RuntimeException("파일 저장 실패:" + e.getMessage());
        }
    }

    //상품 등록
    @Transactional
    public Product create(String productCode, String productName, int productBrand, int productCategory,
                          String productMaterial, String productColor, //int productStock,
                          int productPrice, String productDescription, MultipartFile mainImage,
                          List<MultipartFile> detailImages, int productGender, boolean isSeasonal) {

        String mainImageUrl = saveFile(mainImage);

        List<String> detailImageUrls = new ArrayList<>();
        if(detailImages != null){
            for(MultipartFile file: detailImages){
                String url = saveFile(file);
                if(url != null){
                    detailImageUrls.add(url);
                }
            }
        }

        Product product = new Product();

        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setProductCategory(productBrand);
        product.setProductCategory(productCategory);
        product.setProductMaterial(productMaterial);
        product.setProductColor(productColor);
//        product.setProductStock(productStock);
        product.setProductPrice(productPrice);
        product.setProductDescription(productDescription);
        product.setMainImageUrl(mainImageUrl);
        //detailImageUrls에 ,로 여러 이미지를 한 문자열로 만듬
        product.setDetailImageUrls(String.join(",", detailImageUrls));
        product.setProductGender(productGender);
        product.setProductIsSeasonal(isSeasonal);
        product.setProductStatus(0);

        return productRepository.save(product);
    }
    //상품 목록 페이지버전
    public Page<Product> getProductPage(Pageable pageable) {
    // Sort 객체를 생성하여 id 기준 내림차순 정렬
    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    // 기존 Pageable에 정렬 조건을 추가
    PageRequest pageRequest = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        sort
    );
    return productRepository.findAll(pageRequest);
}

    //상품 상세페이지
    public Product getProduct(Long productId){
        Optional<Product> optionalProduct = this.productRepository.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new RuntimeException("상품이 존재하지 않습니다.");
        }
        Product product = optionalProduct.get();
        return product;
    }

    //상품 삭제
    public void delete(Long productId){
        Optional<Product> optionalProduct = this.productRepository.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new RuntimeException("상품이 존재하지 않습니다.");
        }
        Product product = optionalProduct.get();
        productRepository.delete(product);
    }

    //상품 수정
    @Transactional
    public Product update(Long productId, String productName, int productPrice,
                          int productCategory,String productColor, String productMaterial,
                          int productGender, boolean productIsSeasonal, String productDescription,
                          int productStatus){

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new RuntimeException("상품이 존재하지 않습니다.");
        }
        Product product = optionalProduct.get();


        product.setProductName(productName);
        product.setProductPrice(productPrice);
        product.setProductCategory(productCategory);
        product.setProductColor(productColor);
        product.setProductMaterial(productMaterial);
        product.setProductGender(productGender);
        product.setProductIsSeasonal(productIsSeasonal);
        product.setProductDescription(productDescription);
        product.setProductStatus(productStatus);

        return productRepository.save(product);
    }

    //상품 추천
    public List<Product> getRandomProducts(int count){
        return productRepository.findRandomProducts(count);
    }

    //시즌 상품 추천
    public List<Product> getSeasonalProducts(int count){
        return productRepository.findSeasonalProducts(count);
    }

    //남자 상품 추천
    public List<Product> getMenProducts(int count){
        return productRepository.findByGender(1, count);
    }

    public List<Product> getWomenProducts(int count){
        return productRepository.findByGender(2, count);
    }


    //브랜드로 상품 호출
    public Page<Product> getProductsByBrand(int brand,Pageable pageable){
        return productRepository.findByProductBrand(brand, pageable);
    }

    //카테고리로 상품 호출
    public Page<Product> getProductsByCategory(int category, Pageable pageable){
        return productRepository.findByProductCategory(category, pageable);
    }
}
