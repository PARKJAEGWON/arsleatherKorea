package com.groo.kmw.domain.admin.adminMember.controller;

import com.groo.kmw.domain.front.member.entity.Member;
import com.groo.kmw.domain.front.member.service.MemberService;
import com.groo.kmw.domain.front.order.entity.Order;
import com.groo.kmw.domain.front.order.service.OrderService;
import com.groo.kmw.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/kmw/member")
public class AdminMemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final OrderService orderService;

    @GetMapping("")
    public String adminMemberList(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10") int size, Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberService.findAllMembers(pageable);

        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", memberPage.getTotalPages());

        return "admin/member/adminMemberList";
    }

    @GetMapping("/detail")
    public String adminMemberDetail(@RequestParam Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member", member);

        List<Order> orders = orderService.orderList(id);
        model.addAttribute("orders", orders);

        return "admin/member/adminMemberDetail";
    }
}
