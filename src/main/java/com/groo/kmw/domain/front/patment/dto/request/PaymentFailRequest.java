package com.groo.kmw.domain.front.patment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentFailRequest {

    private String temporaryOrderId;

    private String failReason;

    private String errorCode;

    private String errorMessage;
}
