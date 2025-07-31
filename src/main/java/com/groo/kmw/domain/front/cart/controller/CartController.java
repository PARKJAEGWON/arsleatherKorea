package com.groo.kmw.domain.front.cart.controller;

import com.groo.kmw.domain.front.cart.entity.Cart;
import com.groo.kmw.domain.front.cart.service.CartService;
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final JwtProvider jwtProvider;

    //장바구니 페이지 이동
    @GetMapping()
    public String cartPage(Model model, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId = Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if (memberId == null){
            return "redirect:/member/login";
        }
        List<Cart> cartItems = cartService.getCartItems(memberId);

        // 총 주문 금액을 위한 코드
        int totalPrice = 0;
        for(Cart cart : cartItems) {
            int itemPrice = cart.getProduct().getProductPrice();
            int quantity =cart.getQuantity();
            totalPrice += (itemPrice * quantity);
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "front/cart/cart";
    }

    //장바구니 상품 추가
    @PostMapping("/add")
    public String addCart(@RequestParam Long productId, @RequestParam int quantity, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId = Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null){
            return "redirect:/member/login";
        }
        cartService.addCart(memberId, productId, quantity);
        return "redirect:/cart";
    }
    //장바구니 목록 삭제
    @PostMapping("/delete")
    public String deleteCart(@RequestParam Long cartId, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId = Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null){
            return "redirect:/member/login";
        }
        try {
            cartService.deleteCart(cartId, memberId);
            return "redirect:/cart";
        }catch (RuntimeException e){
            return "redirect:/cart?error=" + e.getMessage();
        }
    }
    //장바구니 상품 수정
    @PostMapping("/update")
    public String updateCart(@RequestParam Long cartId, @RequestParam int quantity, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if (cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId = Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null) {
            return "redirect:/member/login";
        }
        try {
            cartService.updateCart(cartId, memberId , quantity);
            return "redirect:/cart";
        }catch (RuntimeException e){
            return "redirect:/cart?error=" + e.getMessage();
        }
    }
}
