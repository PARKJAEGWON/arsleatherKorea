package com.groo.kmw.domain.admin.product.controller;


import com.groo.kmw.domain.admin.product.dto.request.ProductCreateRequest;
import com.groo.kmw.domain.admin.product.dto.request.ProductUpdateRequest;
import com.groo.kmw.domain.admin.product.entity.Product;
import com.groo.kmw.domain.admin.product.service.ProductService;
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/kmw/product")
public class ProductController {

    private final ProductService productService;
    private final JwtProvider jwtProvider;


    //상품 리스트페이지로 이동
    @GetMapping("")
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductPage(pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "admin/product/adminProductList";
    }

    //상품 상세페이지로 이동
    @GetMapping("/detail")
    public String detail(@RequestParam("id")Long id, Model model){
        Product product = productService.getProduct(id);

        model.addAttribute("product", product);

        return "admin/product/adminProductDetail";
    }

    //상품 등록페이지로 이동
    @GetMapping("/create")
    public String createPage(){
        return "admin/product/adminProductCreate";
    }
    //상품 등록
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute ProductCreateRequest productCreateRequest, BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        Cookie[] cookies = httpServletRequest.getCookies();
        Long adminId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adminAccessToken")) {
                    String token = cookie.getValue();

                    //토큰 유효성 검증으로 보완성을 높여주는게 좋음 습관들여야함
                    if (!jwtProvider.verify(token)) {
                        return "redirect:/admin/kmw/login";
                    }
                    //페이로드안에 있는 json형식의 "" 부분이 키 값이 오브젝트
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    adminId = Long.valueOf(claims.get("adminId").toString());
                    break;
                }
            }
        }
        if (adminId == null) {
            return "redirect:/admin/kmw/login";
        }
        
        try {
            if(productCreateRequest.getMainImage() == null || productCreateRequest.getMainImage().isEmpty()) {
                return "redirect:/admin/kmw/product/create?error=메인 이미지는 필수입니다.";
            }
            
            this.productService.create(
                    productCreateRequest.getProductCode(),
                    productCreateRequest.getProductName(),
                    productCreateRequest.getProductBrand(),
                    productCreateRequest.getProductCategory(),
                    productCreateRequest.getProductMaterial(),
                    productCreateRequest.getProductColor(),
//                    productCreateRequest.getProductStock(),
                    productCreateRequest.getProductPrice(),
                    productCreateRequest.getProductDescription(),
                    productCreateRequest.getMainImage(),
                    productCreateRequest.getDetailImages(),
                    productCreateRequest.getProductGender(),
                    productCreateRequest.isProductIsSeasonal()
            );
            return "redirect:/admin/kmw/product";
        } catch (RuntimeException e) {
            e.printStackTrace(); // 로그 확인을 위해 추가
            // URL 인코딩 처리
            String errorMessage = "파일 업로드에 실패했습니다";
            return "redirect:/admin/kmw/product/create?error=" + errorMessage;
        }
    }
//    @GetMapping("update")
//    public String updatePage(){
//        return "admin/product/adminProductCreate";
//    }
    //상품 삭제
    @PostMapping("/delete")
    public String delete(@RequestParam Long productId){
        try{
            productService.delete(productId);
            return "redirect:/admin/kmw/product";
        }catch(RuntimeException e){
            return "redirect:/admin/kmw/product?error=" + e.getMessage();
        }
    }

    //상품 수정
    @PostMapping("/update")
    public String update(@ModelAttribute ProductUpdateRequest productUpdateRequest, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "admin/product/adminProductCreate";
        }
        Cookie[] cookies = httpServletRequest.getCookies();
        String adminAccessToken = null;
        if(cookies != null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals("adminAccessToken")){
                    adminAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(adminAccessToken == null){
            return "redirect:/admin/kmw/login";
        }
        try {
            if (!jwtProvider.verify(adminAccessToken)) {
                return "redirect:/admin/kmw/login";
            }
            Map<String, Object> claims = jwtProvider.getClaims(adminAccessToken);
            Long adminId = Long.valueOf(claims.get("adminId").toString());

            this.productService.update(
                    productUpdateRequest.getProductId(),
                    adminId,
                    productUpdateRequest.getProductName(),
                    productUpdateRequest.getProductPrice(),
                    productUpdateRequest.getProductCategory(),
                    productUpdateRequest.getProductColor(),
                    productUpdateRequest.getProductMaterial(),
                    productUpdateRequest.getProductGender(),
                    productUpdateRequest.isProductIsSeasonal(),
                    productUpdateRequest.getProductDescription(),
                    productUpdateRequest.getProductStatus()
            );
            return "redirect:/admin/kmw/product/detail?id=" + productUpdateRequest.getProductId();
        } catch (Exception e) {
            httpServletRequest.setAttribute("error", e.getMessage());
            return "redirect:/admin/kmw/product/detail?id=" + productUpdateRequest.getProductId();
        }
    }
}
