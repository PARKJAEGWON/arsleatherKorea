package com.groo.kmw.domain.front.cart.service;

import com.groo.kmw.domain.admin.product.entity.Product;
import com.groo.kmw.domain.admin.product.service.ProductService;
import com.groo.kmw.domain.front.cart.entity.Cart;
import com.groo.kmw.domain.front.cart.repository.CartRepository;
import com.groo.kmw.domain.front.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    //장바구니 추가
    @Transactional
    public void addCart(Long memberId, Long productId, int quantity){
        Cart    cart = new Cart();
        Member member = new Member();
        member.setId(memberId);

        Product product = productService.getProduct(productId);

        cart.setMember(member);
        cart.setProduct(product);
        cart.setQuantity(quantity);

        cartRepository.save(cart);
    }
    //장바구니 목록 조회
    @Transactional(readOnly = true)
    public List<Cart> getCartItems(Long memberId){
        return cartRepository.findByMemberId(memberId);
    }
    //장바구니 목록 삭제
    @Transactional
    public void deleteCart(Long cartId, Long memberId){
        Optional<Cart> optionalCart =cartRepository.findById(cartId);

        if(optionalCart.isEmpty()){
            throw new RuntimeException("장바구니에 상품이 존재하지 않습니다.");
        }
        Cart cart = optionalCart.get();

        if(!cart.getMember().getId().equals(memberId)){
            throw new RuntimeException("권한이 없습니다.");
        }
        cartRepository.deleteById(cartId);
    }
    //장바구니 상품 수정
    @Transactional
    public Cart updateCart(Long cartId,Long memberId, int quantity){
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isEmpty()){
            throw new RuntimeException("장바구니에 상품이 존재하지 않습니다.");
        }
        Cart cart = optionalCart.get();

        if(!cart.getMember().getId().equals(memberId)){
            throw new RuntimeException("권한이 없습니다.");
        }

        if(quantity < 1) {
            throw new RuntimeException("수량은 1개 이상이어야 합니다.");
        }

        cart.setQuantity(quantity);

        return cartRepository.save(cart);
    }
}
