package com.groo.kmw.global.security;

import com.groo.kmw.domain.front.member.service.MemberService;
import com.groo.kmw.domain.admin.admin.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private final AdminService adminService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // admin 경로 체크
        boolean isAdminPath = request.getRequestURI().startsWith("/admin/kmw/");
        
        String memberAccessToken = _getCookie("memberAccessToken");
        String adminAccessToken = _getCookie("adminAccessToken");

        if (isAdminPath) {
            // admin 경로는 adminAccessToken만 처리
            if (adminAccessToken != null && !adminAccessToken.isBlank()) {
                // 토큰 유효기간 검증
                if (!adminService.validateToken(adminAccessToken)) {
                    String refreshToken = _getCookie("adminRefreshToken");

                    if (refreshToken != null && !refreshToken.isBlank()) {
                        String newAccessToken = adminService.refreshAccessToken(refreshToken);
                        _addHeaderCookie("adminAccessToken", newAccessToken, true);
                        adminAccessToken = newAccessToken;
                    }
                }
                // AdminSecurityUser 가져오기
                AdminSecurityUser adminSecurityUser = adminService.getUserFromAccessToken(adminAccessToken);
                // 인가 처리
                SecurityContextHolder.getContext().setAuthentication(adminSecurityUser.genAuthentication());
            }
            // 어드민 액세스 토큰이 없지만 리프레시 토큰이 있는 경우
            else {
                String refreshToken = _getCookie("adminRefreshToken");
                if (refreshToken != null && !refreshToken.isBlank()) {
                    String newAccessToken = adminService.refreshAccessToken(refreshToken);
                    _addHeaderCookie("adminAccessToken", newAccessToken, true);
                    adminAccessToken = newAccessToken;
                    
                    // AdminSecurityUser 가져오기
                    AdminSecurityUser adminSecurityUser = adminService.getUserFromAccessToken(adminAccessToken);
                    // 인가 처리
                    SecurityContextHolder.getContext().setAuthentication(adminSecurityUser.genAuthentication());
                }
            }
        } else {
            // 일반 경로는 memberAccessToken 처리
            if (memberAccessToken != null && !memberAccessToken.isBlank()) {
                // 기존 멤버 토큰 처리 로직...
                if (!memberService.validateToken(memberAccessToken)) {
                    String refreshToken = _getCookie("memberRefreshToken");

                    if (refreshToken != null && !refreshToken.isBlank()) {
                        String newAccessToken = memberService.refreshAccessToken(refreshToken);
                        _addHeaderCookie("memberAccessToken", newAccessToken, false);
                        memberAccessToken = newAccessToken;
                    }
                }
                SecurityUser securityUser = memberService.getUserFromAccessToken(memberAccessToken);
                SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
            }
            else {
                // 기존 멤버 리프레시 토큰 처리 로직...
                String refreshToken = _getCookie("memberRefreshToken");
                if (refreshToken != null && !refreshToken.isBlank()) {
                    String newAccessToken = memberService.refreshAccessToken(refreshToken);
                    _addHeaderCookie("memberAccessToken", newAccessToken, false);
                    memberAccessToken = newAccessToken;
                    
                    SecurityUser securityUser = memberService.getUserFromAccessToken(memberAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String _getCookie(String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;  // 쿠키가 없는 경우 null 반환

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }

    private void _addHeaderCookie(String tokenName, String token, boolean isAdmin) {
        // isAdmin이 true면 어드민 토큰, false면 멤버 토큰
        int maxAge;
        if (isAdmin) {
            maxAge = tokenName.equals("adminAccessToken") ? 60 * 60 : 60 * 60 * 24; // 어드민: 액세스 1시간, 리프레시 1일
        } else {
            maxAge = tokenName.equals("memberAccessToken") ? 60 * 60 : 60 * 60 * 24 * 7; // 멤버: 액세스 1시간, 리프레시 7일
        }

        ResponseCookie cookie = ResponseCookie.from(tokenName, token)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(maxAge)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }
}