<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="main-header">
    <nav class="top-nav">
            <div class="nav-left">
                            <a href="${pageContext.request.contextPath}/" class="logo">ARS LEATHER</a>
                        </div>
                        <div class="nav-center">
                            <ul class="main-menu">
                                <li><a href="/">Home</a></li>
                                <li><a href="/productPost">Collection</a></li>
                                <li><a href="/about">About</a></li>
                                <li><a href="/notice">Notice</a></li>
                                <!-- <li><a href="#">Q&A</a></li> -->
                            </ul>
                        </div>
            <div class="nav-right">
           <!-- <a href="#" class="search-icon">검색</a> -->
            <%
                Cookie[] cookies = request.getCookies();
                boolean isLoggedIn = false;

                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("memberAccessToken".equals(cookie.getName())) {
                            isLoggedIn = true;
                            break;
                        }
                    }
                }
            %>
            <% if (isLoggedIn) { %>
                <!-- 로그인 상태일 때 -->
                <a href="${pageContext.request.contextPath}/member/my" class="account-icon">내 정보</a>
                <a href="${pageContext.request.contextPath}/member/logout" class="account-icon">로그아웃</a>
                <a href="${pageContext.request.contextPath}/cart" class="cart-icon">장바구니</a>
            <% } else { %>
                <!-- 비로그인 상태일 때 -->
                <a href="${pageContext.request.contextPath}/member/login" class="account-icon">로그인</a>
                <a href="${pageContext.request.contextPath}/member/signup" class="account-icon">회원가입</a>
            <% } %>
        </div>
    </nav>
</header>