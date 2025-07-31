package com.groo.kmw.domain.front.patment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPrepareRequest {

    private Integer amount;

    private String orderName;
    private Long memberId;           // 회원 ID 추가

    // 배송 정보
    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address1;
    private String address2;
    private String deliveryRequest;
    // 상품 정보
    private Long productId;           // 상품 ID
    private Integer quantity;         // 수량

}
