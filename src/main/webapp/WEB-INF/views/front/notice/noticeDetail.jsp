<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/noticeDetail.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/front/common/header.jsp" />
<main>
    <div class="notice-detail-container">
        <div class="notice-detail-title">${notice.noticeTitle}</div>
        <div class="notice-detail-date">
            <fmt:parseDate value="${notice.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/>
        </div>
        <div class="notice-detail-content">
            <c:out value="${notice.noticeContent}" escapeXml="false"/>
        </div>
        <div class="notice-divider"></div>
        <div class="notice-button-wrap">
            <a href="${pageContext.request.contextPath}/notice" class="list-button">목록</a>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html> 