package com.groo.kmw.domain.front.order.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderCreateRequest {
    private Long productId;
    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address1;
    private String address2;
    private String deliveryRequest;
    private int quantity;
    private String depositorName;
    private String payMethod;
}
