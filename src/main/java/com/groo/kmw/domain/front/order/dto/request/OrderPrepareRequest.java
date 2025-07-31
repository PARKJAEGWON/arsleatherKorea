package com.groo.kmw.domain.front.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderPrepareRequest {
    private List<OrderItemRequest> orderItems;  // 주문 상품 목록
    private String receiverName;      // 수령인
    private String receiverPhone;     // 연락처
    private String zipCode;           // 우편번호
    private String address1;          // 주소
    private String address2;          // 상세주소
    private String deliveryRequest;   // 배송요청사항
}
