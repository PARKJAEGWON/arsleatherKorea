package com.groo.kmw.domain.front.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    private Long productId;           // 상품 ID

    private Integer quantity;         // 수량

}
