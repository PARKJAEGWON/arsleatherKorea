package com.groo.kmw.domain.front.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartOrderCreateRequest {

    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address1;
    private String address2;
    private String deliveryRequest;
    private String payMethod;
    private String depositorName;
}
