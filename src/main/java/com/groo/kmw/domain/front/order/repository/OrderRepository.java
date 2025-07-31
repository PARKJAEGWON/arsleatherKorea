package com.groo.kmw.domain.front.order.repository;

import com.groo.kmw.domain.front.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    //회원아이디로 조회
    List<Order> findByMemberId(Long memberId);
    //주문번호로 조회
    Optional<Order> findByOrderNumber(String orderNumber);

    //회원 조회 쿼리로 상품도 가져오기
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.member.id = :memberId ORDER BY o.createDateTime DESC")
    List<Order> findByMemberIdWithItemsAndProduct(@Param("memberId") Long memberId);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.id = :orderId")
    Optional<Order> findByIdWithItemsAndProduct(@Param("orderId") Long orderId);

    List<Order> findAllByOrderByCreateDateTimeDesc();

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product ORDER BY o.createDateTime DESC")
    Page<Order> findAllWithItemsAndProduct(Pageable pageable);
}
