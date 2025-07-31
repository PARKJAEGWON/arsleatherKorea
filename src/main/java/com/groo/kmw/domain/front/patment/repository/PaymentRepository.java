package com.groo.kmw.domain.front.patment.repository;

import com.groo.kmw.domain.front.patment.entity.Payment;
import com.groo.kmw.domain.front.patment.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByPaymentKey(String paymentKey);

    List<Payment> findByStatus(PaymentStatus status); // 결제 정보 목록 조회
}
