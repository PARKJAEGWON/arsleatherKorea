<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="top-navbar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/admin/kmw" class="admin-link">ARS LEATHER</a>
    </div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/">[사이트 바로가기]</a>
        <%
            Cookie[] cookies = request.getCookies();
            boolean isLoggedIn = false;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("adminAccessToken".equals(cookie.getName())) {
                        isLoggedIn = true;
                        break;
                    }
                }
            }
        %>
        <% if (isLoggedIn) { %>
            <a href="${pageContext.request.contextPath}/admin/kmw/logout">[로그아웃]</a>
            <a href="javascript:void(0)" onclick="showPasswordChangeModal()">[비밀번호 변경]</a>
        <% } else { %>
            <a href="${pageContext.request.contextPath}/admin/kmw/login">[로그인]</a>
        <% } %>
    </div>
</div>
<div class="sidebar">
    <ul>
        <li class="menu-item"><a href="${pageContext.request.contextPath}/admin/kmw/order">주문관리</a></li>
        <li class="menu-item"><a href="${pageContext.request.contextPath}/admin/kmw/product">상품관리</a></li>
        <li class="menu-item"><a href="${pageContext.request.contextPath}/admin/kmw/member">회원관리</a></li>
        <li class="menu-item"><a href="${pageContext.request.contextPath}/admin/kmw/notice">게시판관리</a></li>
        <!-- <li class="menu-item"><a href="${pageContext.request.contextPath}/admin/kmw/inquiry">문의관리</a></li> -->
    </ul>
</div>