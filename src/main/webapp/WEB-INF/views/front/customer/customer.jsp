<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>고객센터</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/customer.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main class="about-container">
        <img src="${pageContext.request.contextPath}/static/img/customer.jpg">
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html>