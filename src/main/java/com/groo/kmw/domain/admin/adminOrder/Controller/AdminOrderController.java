package com.groo.kmw.domain.admin.adminOrder.Controller;

import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.order.service.OrderService;
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/kmw/order")
public class AdminOrderController {

    private final OrderService orderService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    public String adminOrderList(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size, Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderService.findAllOrders(pageable);

        //현재 페이지의 주문목록데이터 가져오기
        model.addAttribute("orders", orderPage.getContent());
        //현재페이지번호를 0부터 시작
        model.addAttribute("currentPage", page);
        //전체페이지 수
        model.addAttribute("totalPages", orderPage.getTotalPages());
//        //전체 주문 수
//        model.addAttribute("totalItems", orderPage.getTotalElements());
        //한페이지에서 보여줄 수 있는 수
//        model.addAttribute("pageSize", size);

        return "admin/order/adminOrderList";
    }


    @PostMapping("update/status")
    public String orderStatusUpdate(@RequestParam Long orderId, @RequestParam int orderStatus, HttpServletRequest httpServletRequest, Model model){
        Cookie[] cookies = httpServletRequest.getCookies();
        String adminAccessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adminAccessToken")){
                    adminAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(adminAccessToken == null){
            return "redirect:/admin/kmw/login";
        }
        try {
            Map<String, Object> claims = jwtProvider.getClaims(adminAccessToken);
            Long adminId = ((Integer)claims.get("adminId")).longValue();

            orderService.orderStatusUpdate(orderId, orderStatus);
            model.addAttribute("message","상태가 변경되었습니다");
        }catch (Exception e){
            model.addAttribute("error","상태 변경에 실패했습니다.");
        }
        return "redirect:/admin/kmw/order";
    }

    @PostMapping("tracking")
    public String saveTracking(@Valid @RequestParam Long orderId, @RequestParam String trackingNumber, Model model, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        String adminAccessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adminAccessToken")){
                    adminAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(adminAccessToken == null){
            return "redirect:/admin/kmw/login";
        }
        try {
            Map<String, Object> claims = jwtProvider.getClaims(adminAccessToken);
            Long adminId = ((Integer)claims.get("adminId")).longValue();

            orderService.saveTrackingNumber(orderId, trackingNumber);
            model.addAttribute("message","운송장번호가 저장되었습니다.");
        }catch (Exception e){
            model.addAttribute("error","운송장번호 저장에 실패했습니다.");
        }
        return "redirect:/admin/kmw/order";
    }

    @GetMapping("/detail")
    public String getOrderDetail(@RequestParam Long orderId, Model model,HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String adminAccessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adminAccessToken")) {
                    adminAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if (adminAccessToken == null) {
            return "redirect:/admin/kmw/login";
        }
        try {
            Map<String, Object> claims = jwtProvider.getClaims(adminAccessToken);
            Long adminId = ((Integer) claims.get("adminId")).longValue();

            Order order = orderService.getOrderById(orderId);
            model.addAttribute("order", order);
            return "admin/order/adminOrderDetail";
        } catch (IllegalArgumentException e) {
            // 주문을 찾을 수 없는 경우
            return "redirect:/admin/kmw/order?error=주문을 찾을 수 없습니다.";
        }
    }
}
