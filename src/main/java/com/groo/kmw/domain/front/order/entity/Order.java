package com.groo.kmw.domain.front.order.entity;

import com.groo.kmw.domain.front.member.entity.Member;
import com.groo.kmw.domain.front.patment.entity.Payment;
import com.groo.kmw.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //casscaode 영속성 전이 order에 대한 변경사항이 orderItems에도 전판된다
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private String orderNumber; // 자동생성 어떻게 되는지 확인해야함

    private int orderStatus; // 0 배송준비중 1 배송중 2 배송완료 3 구매확정 6.반품 처리 중 7.반품 완료 8.취소 처리 중 9 주문취소

    private String trackingNumber;//운송장번호

    private int totalAmount;


    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address1;
    private String address2;
    private String deliveryRequest;

    private String depositor_name;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
}
