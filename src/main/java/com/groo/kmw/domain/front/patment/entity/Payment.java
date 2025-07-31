package com.groo.kmw.domain.front.patment.entity;

import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.patment.entity.enums.PaymentStatus;
import com.groo.kmw.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Payment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;    // 실제 주문과의 연관관계

    // 추가 필요한 필드들:
    private String paymentKey;         // 토스페이먼츠 결제 키
    private Integer amount;            // 결제금액
    private String tossPaymentsOrderId;  // 토스페이먼츠 주문번호 (TEMP_timestamp)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;      // 결제상태

    @Column(columnDefinition = "TEXT")
    private String orderData;          // 주문 데이터 임시 저장용

    private String paymentMethod;      // 결제수단
    private String cardNumber;         // 카드번호
    private String cardCompany;        // 카드사

    private String cardType;           // 카드 종류 (신용, 체크 등)
    
    private String failReason;         // 실패사유
    
    private Integer installmentPlanMonths = 0;  // 할부 개월 수, 기본값 0
    
    private Integer totalAmount = 0;    // 총 결제 금액, 기본값 0
}
