package com.groo.kmw.domain.front.cart.repository;

import com.groo.kmw.domain.front.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    //회원이 담은 장바구니 목록 조회
    List<Cart> findByMemberId(Long memberId);
    //회원이 틀정 상품을 장바구니 담았는지 조회
    Cart findByMemberIdAndProductId(Long memberId, Long productId);
}
