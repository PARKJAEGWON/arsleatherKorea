package com.groo.kmw.domain.front.patment.controller;

import com.groo.kmw.domain.front.order.dto.request.CartOrderCreateRequest;
import com.groo.kmw.domain.front.order.dto.request.DirectOrderCreateRequest;
import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.patment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    
    private final PaymentService paymentService;

    @GetMapping("/success")
    public String handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Integer amount,
            @RequestParam String orderType,
            @ModelAttribute DirectOrderCreateRequest directOrderRequest,
            @ModelAttribute CartOrderCreateRequest cartOrderRequest,
            @CookieValue("memberAccessToken") String token) {
        
        try {
            Order order = paymentService.confirmPayment(
                paymentKey, 
                orderId, 
                amount,
                orderType,
                directOrderRequest,
                cartOrderRequest,
                token
            );
            return "redirect:/order/complete?orderId=" + order.getId();
        } catch (Exception e) {
            log.error("Payment confirmation failed", e);
            return "redirect:/payments/fail";
        }
    }

    @GetMapping("/fail")
    public String handlePaymentFail(
            @RequestParam String message,
            @RequestParam String code) {
        log.error("Payment failed: {} ({})", message, code);
        return "redirect:/order/fail?message=" + message;
    }
}