package com.groo.kmw.domain.front.order.service;

import com.groo.kmw.domain.admin.product.entity.Product;
import com.groo.kmw.domain.admin.product.service.ProductService;
import com.groo.kmw.domain.front.cart.entity.Cart;
import com.groo.kmw.domain.front.cart.service.CartService;
import com.groo.kmw.domain.front.member.entity.Member;
import com.groo.kmw.domain.front.member.service.MemberService;
import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.order.entity.OrderItem;
import com.groo.kmw.domain.front.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ProductService productService;
    private final CartService cartService;

    //장바구니 상품 주문
    @Transactional
    public Order createCartOrder(Long memberId, String receiverName, String receiverPhone,
                                 String zipCode, String address1, String address2,
                                 String deliveryRequest, List<Cart> cartItems, String depositorName){

        List<OrderItem> orderItems = new ArrayList<>();

        for(Cart cart : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cart.getProduct());
            orderItem.setQuantity(cart.getQuantity());
            //상품을 카트넣었기 때문에 역순으로 카트에서 상품 상품에서 상품의 가격
            orderItem.setPrice(cart.getProduct().getProductPrice());
            orderItem.setTotalPrice(cart.getProduct().getProductPrice() * cart.getQuantity());
            orderItems.add(orderItem);
        }

        Order order = createOrder(memberId, receiverName, receiverPhone, zipCode, address1, address2, deliveryRequest, orderItems, depositorName);  // 입금자명은 null로 전달

        for(Cart cart : cartItems) {
            cartService.deleteCart(cart.getId(), memberId);
        }
        return order;
    }


    //다이렉트 상품 주문
    @Transactional
    public Order createDirectOrder(Long memberId, Long productId, String receiverName,
                                   String receiverPhone, String zipCode, String address1,
                                   String address2, String deliveryRequest, int quantity,
                                   String depositorName){  // 입금자명 파라미터 추가

        Product product = productService.getProduct(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getProductPrice());
        orderItem.setTotalPrice(product.getProductPrice()* quantity);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        return createOrder(memberId, receiverName, receiverPhone, zipCode, address1, address2, deliveryRequest, orderItems, depositorName);  // 입금자명 전달
    }

    //주문 생성
    @Transactional
    public Order createOrder(Long memberId, String receiverName, String receiverPhone,
                             String zipCode, String address1, String address2,
                             String deliveryRequest, List<OrderItem> orderItems,
                             String depositorName){  // 입금자명 파라미터 추가

        Member member = memberService.findById(memberId);

        Order order = new Order();
        order.setMember(member);
        order.setOrderNumber(generateOrderNumber());
        order.setOrderStatus(0);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setZipCode(zipCode);
        order.setAddress1(address1);
        order.setAddress2(address2);
        order.setDeliveryRequest(deliveryRequest);
        order.setDepositor_name(depositorName);  // 입금자명 설정

        for(OrderItem orderItem : orderItems){
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        order.setTotalAmount(calculateTotalAmount(orderItems));

        return orderRepository.save(order);
    }
    //주문번호 랜덤 생성
    private String generateOrderNumber(){
        // 현재날짜시간
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        //랜덤 숫자 6자리 생성
        String randomNumber = String.format("%06d", new Random().nextInt(1000000));
        return dateTime + randomNumber;
    }
    //주문상품 총 금액 계산
    private int calculateTotalAmount(List<OrderItem> orderItems){
        int totalAmount = 0;
        for(OrderItem item : orderItems) {
            totalAmount += item.getTotalPrice();
        }
        return totalAmount;
    }

    public Order getOrder(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();
        return order;
    }

    //주문 단건 내역
    public Order getOrder(Long orderId, Long memberId){
        Optional<Order> optionalOrder = orderRepository.findByIdWithItemsAndProduct(orderId);
        if(optionalOrder.isEmpty()){
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();

        if(!order.getMember().getId().equals(memberId)){
            throw new RuntimeException("권한이 없습니다.");
        }
        return  order;
    }

    //주문내역 리스트
    public List<Order> orderList(Long memberId){
        return orderRepository.findByMemberIdWithItemsAndProduct(memberId);
    }

    /////////////////////////////////////////////////////////////////////////////////어드민/////////////////////////////////////////////////////
    
    //주문내역 상태 변경
    public void orderStatusUpdate(Long orderId,int orderStatus){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()){
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }
    //운송장 번호 저장
    public void saveTrackingNumber(Long orderId, String trackingNumber){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()){
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);
    }

    //모든 주문내역리스트
    public Page<Order> findAllOrders(Pageable pageable) {

        return orderRepository.findAllWithItemsAndProduct(pageable);
    }

    //아이템 가져오기
    public Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();
        return order;
    }

    //주문 취소 처리
    @Transactional
    public void cancelOrder(Long orderId, Long memberId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();

        if(order.getOrderStatus() != 0){
            throw new RuntimeException("취소할 수 없는 주문입니다.");
        }

        order.setOrderStatus(8);
        orderRepository.save(order);
    }

    //주문 반품 처리
    @Transactional
    public void returnOrder(Long orderId, Long memberId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new RuntimeException("주문내역이 존재하지 않습니다.");
        }
        Order order = optionalOrder.get();

        if(order.getOrderStatus() != 1 && order.getOrderStatus() != 2 ){
            throw new RuntimeException("반품 요청할 수 없는 주문입니다.");
        }

        order.setOrderStatus(6);
        orderRepository.save(order);
    }
}
