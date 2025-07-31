<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 목록</title>
    <link rel="stylesheet" href="/static/css/admin.css">
    <link rel="stylesheet" href="/static/css/adminNotice.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <div style="display: flex; align-items: center; justify-content: space-between;">
            <h2>공지사항 목록</h2>
            <div class="button-group">
                <a href="${pageContext.request.contextPath}/admin/kmw/notice/create" class="btn">공지사항 등록</a>
            </div>
        </div>
        <div class="notice-simple-list">
            <c:choose>
                <c:when test="${empty noticePage.content}">
                    <div class="notice-simple-item" style="justify-content: center; color: #aaa;">게시글이 존재하지 않습니다.</div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="notice" items="${noticePage.content}">
                        <div class="notice-simple-item ${notice.noticeStatus == 1 ? 'top-notice' : ''} ${notice.noticeStatus == 9 ? 'dimmed-notice' : ''}">
                            <a href="${pageContext.request.contextPath}/admin/kmw/notice/detail?id=${notice.id}" class="notice-title-link">
                                 <c:if test="${notice.noticeStatus == 1}"><b></c:if>${notice.noticeTitle}<c:if test="${notice.noticeStatus == 1}"></b></c:if>
                            </a>
                            <div class="notice-date">
                                <fmt:parseDate value="${notice.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        <!-- 페이징 UI -->
        <c:if test="${noticePage.totalPages > 0}">
        <div class="pagination">
            <c:choose>
                <c:when test="${noticePage.number > 0}">
                    <a href="?page=${noticePage.number - 1}&size=${pageSize}" class="page-link">이전</a>
                </c:when>
                <c:otherwise>
                    <span class="page-link disabled">이전</span>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="0" end="${noticePage.totalPages - 1}" var="pageNum">
                <c:if test="${pageNum == noticePage.number}">
                    <span class="page-link active">${pageNum + 1}</span>
                </c:if>
                <c:if test="${pageNum != noticePage.number}">
                    <a href="?page=${pageNum}&size=${pageSize}" class="page-link">${pageNum + 1}</a>
                </c:if>
            </c:forEach>
            <c:choose>
                <c:when test="${noticePage.number < noticePage.totalPages - 1}">
                    <a href="?page=${noticePage.number + 1}&size=${pageSize}" class="page-link">다음</a>
                </c:when>
                <c:otherwise>
                    <span class="page-link disabled">다음</span>
                </c:otherwise>
            </c:choose>
        </div>
        </c:if>
    </div>
</main>
<script src="/static/js/admin.js"></script>
</body>
</html> 