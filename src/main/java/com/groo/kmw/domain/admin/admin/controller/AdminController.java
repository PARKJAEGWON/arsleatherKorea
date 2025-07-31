package com.groo.kmw.domain.admin.admin.controller;

import com.groo.kmw.domain.admin.admin.dto.request.AdminLoginRequest;
import com.groo.kmw.domain.admin.admin.dto.request.AdminSignupRequest;
import com.groo.kmw.domain.admin.admin.dto.request.AdminUpadateRequest;
import com.groo.kmw.domain.admin.admin.entity.Admin;
import com.groo.kmw.domain.admin.admin.service.AdminService;
import com.groo.kmw.domain.front.member.service.MemberService;  // 올바른 import 경로
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/kmw")
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;  // MemberService 주입
    private final JwtProvider jwtProvider;

    @GetMapping("")
    public String adminOrder(){
        return "admin/adminIndex";
    }

//    @GetMapping("/member")
//    public String adminMember(){
//        return "admin/adminMember";
//    }

    //관리자 회원가입 페이지 이동
    @GetMapping("/signup")
    public String adminSignupPage() {
        return "admin/admin/adminSignup";
    }

    //관리자 회원가입
    @PostMapping("/signup")
    public String adminSignup(@Valid AdminSignupRequest adminSignupRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "admin/admin/adminSignup";

        this.adminService.signup(
                adminSignupRequest.getAdminLoginId(),
                adminSignupRequest.getAdminPassword(),
                adminSignupRequest.getAdminName());

        return "redirect:/admin/kmw/login";
    }
    //관리자 회원수정
    @PostMapping("/update")
    public String adminUpdate(@Valid @ModelAttribute AdminUpadateRequest adminUpadateRequest, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
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
        if(bindingResult.hasErrors()){
            return "redirect:/admin/kmw/login";
        }

        try{
            Map<String, Object> claims  = jwtProvider.getClaims(adminAccessToken);
            Long amdinId = ((Integer)claims.get("adminId")).longValue();

            this.adminService.update(amdinId, adminUpadateRequest.getAdminPassword());

            httpServletRequest.setAttribute("message","비밀번호가 성공적으로 수정되었습니다.");
            return "redirect:/admin/kmw";
        } catch (Exception e){
            httpServletRequest.setAttribute("adminUpdate",e.getMessage());
            return "redirect:/admin/kmw";
        }
    }
    //관리자 로그인 페이지 이동
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/admin/adminLogin";
    }

    //관리자 로그인
    @PostMapping("/login")
    public String adminLogin(@Valid AdminLoginRequest adminLoginRequest, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/admin/adminLogin";
        }

        try{
            Admin admin = adminService.login(
                    adminLoginRequest.getAdminLoginId(),
                    adminLoginRequest.getAdminPassword());

            String adminAccessToken = jwtProvider.generateAdminAccessToken(admin);

            Cookie adminAccessCookie = new Cookie("adminAccessToken", adminAccessToken);
            adminAccessCookie.setHttpOnly(true);
            adminAccessCookie.setSecure(true);
            adminAccessCookie.setPath("/");
            adminAccessCookie.setMaxAge(60 * 60);
            httpServletResponse.addCookie(adminAccessCookie);

            Cookie adminRefreshCookie = new Cookie("adminRefreshToken", admin.getAdminRefreshToken());
            adminRefreshCookie.setHttpOnly(true);
            adminRefreshCookie.setSecure(true);
            adminRefreshCookie.setPath("/");
            adminRefreshCookie.setMaxAge(60 * 60 * 24);
            httpServletResponse.addCookie(adminRefreshCookie);

            return "redirect:/admin/kmw";
        } catch (RuntimeException e){
            httpServletRequest.setAttribute("adminLoginError",e.getMessage());
            return "admin/admin/adminLogin";
        }
    }
    //관리자 로그아웃
    @GetMapping("/logout")
    public String adminLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        Cookie[] cookies = httpServletRequest.getCookies();

        String adminAccessToken = "";

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("adminAccessToken")){
                    adminAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(!adminAccessToken.isEmpty()){
            Map<String, Object> claims = jwtProvider.getClaims(adminAccessToken);
            Long adminId = ((Integer)claims.get("adminId")).longValue();
            Admin admin = adminService.findById(adminId);
            adminService.logout(admin);
        }

        Cookie adminAccessCookie = new Cookie("adminAccessToken", null);
        adminAccessCookie.setPath("/");
        adminAccessCookie.setMaxAge(0);
        adminAccessCookie.setHttpOnly(true);
        adminAccessCookie.setSecure(true);
        httpServletResponse.addCookie(adminAccessCookie);

        Cookie adminRefreshCookie = new Cookie("adminRefreshToken", null);
        adminRefreshCookie.setPath("/");
        adminRefreshCookie.setMaxAge(0);
        adminAccessCookie.setHttpOnly(true);
        adminAccessCookie.setSecure(true);
        httpServletResponse.addCookie(adminRefreshCookie);

        return "redirect:/admin/kmw/login";
    }

    @PostMapping("/member/description/{memberId}")
    @ResponseBody
    public Map<String, Object> updateMemberDescription(@PathVariable Long memberId, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String description = request.get("description");
            memberService.updateMemberDescription(memberId, description);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}