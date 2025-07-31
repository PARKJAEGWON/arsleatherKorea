<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 상세</title>
    <link rel="stylesheet" href="/static/css/admin.css">
    <link rel="stylesheet" href="/static/css/adminNotice.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <h1 class="notice-main-title">공지사항</h1>
        <div class="notice-detail-header">
            <h2 class="notice-title">${notice.noticeTitle}</h2>
            <div class="notice-meta">
                <span class="notice-writer">${notice.admin.adminName}</span>
                <span class="notice-status 
                    ${notice.noticeStatus == 0 ? 'status-normal' : ''} 
                    ${notice.noticeStatus == 1 ? 'status-top' : ''} 
                    ${notice.noticeStatus == 9 ? 'status-hidden' : ''}">
                    <c:choose>
                        <c:when test="${notice.noticeStatus == 0}">일반</c:when>
                        <c:when test="${notice.noticeStatus == 1}">최상위</c:when>
                        <c:when test="${notice.noticeStatus == 9}">숨김</c:when>
                        <c:otherwise>기타</c:otherwise>
                    </c:choose>
                </span>
                <span class="notice-date">
                    <fmt:parseDate value="${notice.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                    <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/>
                </span>
            </div>
        </div>
        <hr class="notice-divider" />
        <div class.="notice-content" style="margin-bottom:32px;">
            ${notice.noticeContent}
        </div>
        <div class="notice-divider"></div>
        <div class="button-group">
            <a href="${pageContext.request.contextPath}/admin/kmw/notice/update?id=${notice.id}" class="btn">수정</a>
            <form action="${pageContext.request.contextPath}/admin/kmw/notice/delete" method="post" style="display:inline;">
                <input type="hidden" name="id" value="${notice.id}" />
                <button type="submit" class="btn" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
            </form>
        </div>
    </div>
</main>
<script src="/static/js/admin.js"></script>
</body>
</html> 