package com.groo.kmw.domain.front.order.controller;

import com.groo.kmw.domain.admin.product.entity.Product;
import com.groo.kmw.domain.admin.product.service.ProductService;
import com.groo.kmw.domain.front.cart.entity.Cart;
import com.groo.kmw.domain.front.cart.service.CartService;
import com.groo.kmw.domain.front.member.entity.Member;
import com.groo.kmw.domain.front.member.service.MemberService;
import com.groo.kmw.domain.front.order.dto.request.CartOrderCreateRequest;
import com.groo.kmw.domain.front.order.dto.request.DirectOrderCreateRequest;
import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.order.service.OrderService;
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final JwtProvider jwtProvider;
    private final ProductService productService;
    private final MemberService memberService;

    //장바구니 주문 페이지 이동
    @GetMapping("/cart")
    public String cartOrderPage(Model model, HttpServletRequest httpServletRequest){

        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
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
        Member member = memberService.findById(memberId);

        int totalPrice = 0;
        for(Cart cart : cartItems){
            totalPrice += (cart.getProduct().getProductPrice() * cart.getQuantity());
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("member", member);
        return "front/order/order";
    }
    //장바구니 상품 주문
    @PostMapping("/cart")
    public String createCartOrder(@ModelAttribute CartOrderCreateRequest cartOrderCreateRequest, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
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

        List<Cart> cartItems = cartService.getCartItems(memberId);

        try {
            Order order = orderService.createCartOrder(
                    memberId,
                    cartOrderCreateRequest.getReceiverName(),
                    cartOrderCreateRequest.getReceiverPhone(),
                    cartOrderCreateRequest.getZipCode(),
                    cartOrderCreateRequest.getAddress1(),
                    cartOrderCreateRequest.getAddress2(),
                    cartOrderCreateRequest.getDeliveryRequest(),
                    cartItems,
                    cartOrderCreateRequest.getDepositorName()
            );
            return "redirect:/order/complete?orderId=" + order.getId();
        } catch (RuntimeException e){
            return "redirect:/order/cart?error" + e.getMessage();
        }
    }

    //다이렉트 상품 주문 페이지 이동
    @GetMapping("/direct")
    public String directOrderPage(@RequestParam Long productId, @RequestParam int quantity, Model model, HttpServletRequest httpServletRequest){
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
        if (memberId == null){
            return "redirect:/member/login";
        }
        Product product = productService.getProduct(productId);
        int totalPrice = product.getProductPrice() * quantity;
        Member member = memberService.findById(memberId);

        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("member", member);
        return "front/order/order";
    }

    //다이렉트 상품 주문
    @PostMapping("/direct")
    public String createDirectOrder(@ModelAttribute DirectOrderCreateRequest directOrderCreateRequest, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("memberAccessToken")){
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
            Order order = orderService.createDirectOrder(
                    memberId,
                    directOrderCreateRequest.getProductId(),
                    directOrderCreateRequest.getReceiverName(),
                    directOrderCreateRequest.getReceiverPhone(),
                    directOrderCreateRequest.getZipCode(),
                    directOrderCreateRequest.getAddress1(),
                    directOrderCreateRequest.getAddress2(),
                    directOrderCreateRequest.getDeliveryRequest(),
                    directOrderCreateRequest.getQuantity(),
                    directOrderCreateRequest.getDepositorName()  // 입금자명 전달
            );
            return "redirect:/order/complete?orderId=" + order.getId();
        } catch (RuntimeException e) {
            return "redirect:/order/direct?error=" + e.getMessage();
        }
    }
    //주문완료 페이지
    @GetMapping("/complete")
    private String orderComplete(@RequestParam Long orderId, Model model){
        Order order = orderService.getOrder(orderId);
        model.addAttribute("order", order);
        return "front/order/orderComplete";
    }

    //주문내역페이지
    @GetMapping("/list")
    public String orderList(Model model,HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token =  cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId = Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null){
            return "redirect:/member/login";
        }
        List<Order> orderList = orderService.orderList(memberId);
        model.addAttribute("orderList", orderList);
        return "front/order/orderList";
    }

    //주문 상세내역 조회
    @GetMapping("/detail")
    public String orderDetail(@RequestParam Long orderId, HttpServletRequest httpServletRequest,Model model){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId =Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null){
            return "redirect:/member/login";
        }

        Order order = orderService.getOrder(orderId, memberId);
        model.addAttribute("order", order);

        return "front/order/orderDetail";
    }

    //주문 취소 처리
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId, HttpServletRequest httpServletRequest, Model model){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId =Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null){
            return "redirect:/member/login";
        }
        try {
            orderService.cancelOrder(orderId, memberId);
            return "redirect:/order/list";
            
        }catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/order/list?error=" + e.getMessage();
        }
    }
    
    //주문 반품 처리
    @PostMapping("/{orderId}/return")
    public String returnOrder(@PathVariable Long orderId, HttpServletRequest httpServletRequest, Model model){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long memberId = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    String token = cookie.getValue();
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    memberId =Long.valueOf(claims.get("memberId").toString());
                    break;
                }
            }
        }
        if(memberId == null){
            return "redirect:/member/login";
        }
        try {
            orderService.returnOrder(orderId, memberId);
            return "redirect:/order/list";

        }catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/order/list?error=" + e.getMessage();
        }
    }
}
