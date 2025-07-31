<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/notice.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/front/common/header.jsp" />
<div class="notice-list-container">
    <div class="notice-list-title">공지사항</div>
    <table class="notice-list-table">
        <thead>
            <tr>
                <th>제목</th>
                <th style="width:120px; text-align:right;">작성일</th>
            </tr>
        </thead>
        <tbody>
            <!-- 최상위 공지는 모든 페이지에서 고정 표시 -->
            <c:forEach var="notice" items="${noticePage.content}">
                <c:if test="${notice.noticeStatus == 1}">
                    <tr class="top-notice">
                        <td>
                            <a href="${pageContext.request.contextPath}/notice/detail?id=${notice.id}" class="notice-title-link">
                                [공지] <b>${notice.noticeTitle}</b>
                            </a>
                        </td>
                        <td style="text-align:right; color:#888;">
                            <fmt:parseDate value="${notice.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            
            <!-- 일반 공지 표시 (숨김 제외) -->
            <c:forEach var="notice" items="${noticePage.content}">
                <c:if test="${notice.noticeStatus == 0}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/notice/detail?id=${notice.id}" class="notice-title-link">
                                [공지] ${notice.noticeTitle}
                            </a>
                        </td>
                        <td style="text-align:right; color:#888;">
                            <fmt:parseDate value="${notice.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </tbody>
    </table>
    <div class="pagination">
        <c:if test="${totalPages > 0}">
            <c:if test="${currentPage > 0}">
                <a href="?page=${currentPage-1}">&lt;</a>
            </c:if>
            <c:forEach begin="0" end="${totalPages-1}" var="i">
                <a href="?page=${i}" class="${currentPage == i ? 'active' : ''}">${i+1}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages-1}">
                <a href="?page=${currentPage+1}">&gt;</a>
            </c:if>
        </c:if>
    </div>
</div>
<jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html> 