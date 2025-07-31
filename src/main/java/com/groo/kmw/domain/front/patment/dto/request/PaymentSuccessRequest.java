package com.groo.kmw.domain.front.patment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSuccessRequest {

    private String paymentKey;        // 토스페이먼츠 결제 키

    private String orderId;           // payment ID

    private Integer amount;           // 결제금액
}
