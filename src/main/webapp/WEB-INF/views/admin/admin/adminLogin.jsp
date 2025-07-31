<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>어드민 페이지</title>
    <link rel="stylesheet" href="/static/css/admin.css">
    <link rel="stylesheet" href="/static/css/adminLogin.css">
    <script src="/static/js/adminLogin.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>

    <main>
        <div class="login-container">
            <input type="hidden" id="adminLoginError" value="${adminLoginError}">
            <form action="${pageContext.request.contextPath}/admin/kmw/login" method="post">
                <div class="input-group">
                    <input type="text" name="adminLoginId" placeholder="아이디" required>
                </div>
                <div class="input-group">
                    <input type="password" name="adminPassword" placeholder="비밀번호" required>
                </div>
                <button type="submit" class="login-btn">로그인</button>
            </form>
            <a href="${pageContext.request.contextPath}/admin/kmw/signup" class="signup-btn">회원가입</a>
        </div>
    </main>
    <script src="/static/js/admin.js"></script>
</body>
</html>