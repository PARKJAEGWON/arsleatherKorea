package com.groo.kmw.domain.front.patment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPrepareResponse {

    private String paymentId;         // payment 엔티티의 ID

    private Integer amount;

    private String orderName;
}
