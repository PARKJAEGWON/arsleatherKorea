package com.groo.kmw.domain.front.patment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.groo.kmw.domain.front.cart.entity.Cart;
import com.groo.kmw.domain.front.cart.service.CartService;
import com.groo.kmw.domain.front.order.dto.request.CartOrderCreateRequest;
import com.groo.kmw.domain.front.order.dto.request.DirectOrderCreateRequest;
import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.order.service.OrderService;
import com.groo.kmw.domain.front.patment.entity.Payment;
import com.groo.kmw.domain.front.patment.entity.enums.PaymentStatus;
import com.groo.kmw.domain.front.patment.repository.PaymentRepository;
import com.groo.kmw.global.config.TossPaymentsConfig;
import com.groo.kmw.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    
    private final OrderService orderService;
    private final CartService cartService;
    private final JwtProvider jwtProvider;
    private final PaymentRepository paymentRepository;
    private final TossPaymentsConfig tossConfig;
    private final RestTemplate restTemplate;

    @Transactional
    public Order confirmPayment(
            String paymentKey, 
            String orderId, 
            Integer amount,
            String orderType,
            DirectOrderCreateRequest directOrderRequest,
            CartOrderCreateRequest cartOrderRequest,
            String token) {

        // 1. 회원 ID 추출
        Long memberId = getMemberIdFromToken(token);

        // 2. 결제 정보 생성
        Payment payment = createInitialPayment(orderId, amount);

        try {
            // 3. 토스페이먼츠 결제 승인
            JsonNode tossResponse = requestTossPaymentConfirmation(paymentKey, orderId, amount);

            // 4. 주문 생성
            Order order = createOrder(orderType, memberId, directOrderRequest, cartOrderRequest);

            // 5. 결제 정보 업데이트
            updatePaymentInfo(payment, paymentKey, order, tossResponse);
            
            return order;

        } catch (Exception e) {
            handlePaymentError(payment, e);
            throw new RuntimeException("결제 승인 중 오류 발생", e);
        }
    }

    private Long getMemberIdFromToken(String token) {
        Map<String, Object> claims = jwtProvider.getClaims(token);
        return Long.valueOf(claims.get("memberId").toString());
    }

    private Payment createInitialPayment(String orderId, Integer amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setTotalAmount(amount);
        payment.setTossPaymentsOrderId(orderId);
        payment.setStatus(PaymentStatus.READY);
        return paymentRepository.save(payment);
    }

    private JsonNode requestTossPaymentConfirmation(String paymentKey, String orderId, Integer amount) {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuth = Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(
            "https://api.tosspayments.com/v1/payments/confirm",
            request,
            JsonNode.class
        );

        return response.getBody();
    }

    private Order createOrder(String orderType, Long memberId, 
                            DirectOrderCreateRequest directOrderRequest,
                            CartOrderCreateRequest cartOrderRequest) {
        if ("cart".equals(orderType)) {
            List<Cart> cartItems = cartService.getCartItems(memberId);
            return orderService.createCartOrder(
                memberId,
                cartOrderRequest.getReceiverName(),
                cartOrderRequest.getReceiverPhone(),
                cartOrderRequest.getZipCode(),
                cartOrderRequest.getAddress1(),
                cartOrderRequest.getAddress2(),
                cartOrderRequest.getDeliveryRequest(),
                cartItems,
                null  // 카드결제라서 입금자명 불필요
            );
        } else {
            return orderService.createDirectOrder(
                memberId,
                directOrderRequest.getProductId(),
                directOrderRequest.getReceiverName(),
                directOrderRequest.getReceiverPhone(),
                directOrderRequest.getZipCode(),
                directOrderRequest.getAddress1(),
                directOrderRequest.getAddress2(),
                directOrderRequest.getDeliveryRequest(),
                directOrderRequest.getQuantity(),
                null  // 카드결제라서 입금자명 불필요
            );
        }
    }

    private void updatePaymentInfo(Payment payment, String paymentKey, Order order, JsonNode tossResponse) {
        payment.setPaymentKey(paymentKey);
        payment.setStatus(PaymentStatus.DONE);
        payment.setOrder(order);
        
        if (tossResponse != null && tossResponse.has("card")) {
            JsonNode cardInfo = tossResponse.get("card");
            if (cardInfo.has("number")) {
                payment.setCardNumber(cardInfo.get("number").asText());
            }
            if (cardInfo.has("issuerCode")) {
                payment.setCardCompany(getCardCompanyName(cardInfo.get("issuerCode").asText()));
            }
        }
    }

    private void handlePaymentError(Payment payment, Exception e) {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailReason("결제 승인 실패: " + e.getMessage());
        paymentRepository.save(payment);
        log.error("Payment failed", e);
    }

    private String getCardCompanyName(String issuerCode) {
        return switch (issuerCode) {
            case "11" -> "BC카드";
            case "21" -> "하나카드";
            case "31" -> "삼성카드";
            case "32" -> "KB국민카드";
            case "33" -> "우리카드";
            case "34" -> "신한카드";
            case "35" -> "현대카드";
            case "36" -> "롯데카드";
            case "37" -> "NH농협카드";
            case "38" -> "씨티카드";
            case "39" -> "카카오뱅크";
            case "41" -> "우체국";
            case "42" -> "제주은행";
            case "43" -> "광주은행";
            case "44" -> "전북은행";
            case "45" -> "수협은행";
            case "46" -> "신협";
            case "47" -> "부산은행";
            case "48" -> "대구은행";
            case "51" -> "K뱅크";
            case "52" -> "토스뱅크";
            case "61" -> "SC제일은행";
            case "71" -> "우리카드";
            case "81" -> "KDB산업은행";
            default -> "알 수 없는 카드사";
        };
    }
}