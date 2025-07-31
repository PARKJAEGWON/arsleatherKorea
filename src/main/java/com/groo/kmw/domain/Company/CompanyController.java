package com.groo.kmw.domain.Company;

import com.groo.kmw.domain.admin.product.entity.Product;
import com.groo.kmw.domain.admin.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompanyController {

    private final ProductService productService;

    //메인페이지
    @GetMapping("/")
    public String index(Model model){
        List<Product> seasonalProducts = productService.getSeasonalProducts(15);
        model.addAttribute("seasonalProducts", seasonalProducts);

        List<Product> menProducts = productService.getMenProducts(15);
        model.addAttribute("menProducts", menProducts);

        List<Product> womenProducts = productService.getWomenProducts(15);
        model.addAttribute("womenProducts", womenProducts);
        return "front/index";
    }
    
    //개인정보 수집 이용약관 페이지
    @GetMapping("/terms/privacy")
    public String privacy(){
        return "front/terms/privacy";
    }

    //아르스레더 이용약관 페이지
    @GetMapping("/terms/ars")
    public String ars(){
        return "front/terms/ars";
    }

    //전자금융거래 이용약관 페이지
    @GetMapping("/terms/finance")
    public String finance(){
        return "front/terms/finance";
    }

    //회사 소개
    @GetMapping("/about")
    public String about(){
        return "front/about/about";
    }

    //고객 센터
    @GetMapping("/customer")
    public String customer(){
        return "front/customer/customer";
    }
}
