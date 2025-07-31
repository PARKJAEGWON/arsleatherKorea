<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원관리</title>
    <link rel="stylesheet" href="/static/css/admin.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <!-- <div class="top-bar">
            <div></div>
            <div class="right-btns">
                <input type="text" placeholder="검색어 입력">
                <button>검색</button>
            </div>
        </div> -->
        <h2>회원 목록</h2>
        <table class="admin-table">
            <thead>
            <tr>
                <th>No</th>
                <th>이름</th>
                <th>아이디</th>
                <th>이메일</th>
                <th>연락처</th>
                <th>성별</th>
                <th>생년월일</th>
                <th>상태</th>
                <th>가입일</th>
                <th>관리</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="member" items="${members}" varStatus="status">
                <tr>
                    <td>${member.id}</td>
                    <td>${member.memberName}</td>
                    <td>${member.memberLoginId}</td>
                    <td>${member.memberEmail}</td>
                    <td>${member.memberPhone}</td>
                    <td>
                        <c:choose>
                            <c:when test="${member.memberGender == 'MALE'}">남</c:when>
                            <c:when test="${member.memberGender == 'FEMALE'}">여</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${member.memberBirthDate}</td>
                    <td>
                        <c:choose>
                            <c:when test="${member.memberStatus == 0}">이용중</c:when>
                            <c:when test="${member.memberStatus == 8}">탈퇴</c:when>
                            <c:when test="${member.memberStatus == 9}">정지</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${member.createDateTime.toLocalDate()}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/kmw/member/detail?id=${member.id}">상세</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <!-- 페이징 UI -->
        <div class="pagination">
            <c:if test="${totalPages > 0}">
                <!-- 이전 버튼 -->
                <c:choose>
                    <c:when test="${currentPage > 0}">
                        <a href="?page=${currentPage - 1}&size=${pageSize}" class="page-link">이전</a>
                    </c:when>
                    <c:otherwise>
                        <span class="page-link disabled">이전</span>
                    </c:otherwise>
                </c:choose>

                <!-- 페이지 번호 -->
                <c:forEach begin="0" end="${totalPages - 1}" var="pageNum">
                    <c:if test="${pageNum == currentPage}">
                        <span class="page-link active">${pageNum + 1}</span>
                    </c:if>
                    <c:if test="${pageNum != currentPage}">
                        <a href="?page=${pageNum}&size=${pageSize}" class="page-link">${pageNum + 1}</a>
                    </c:if>
                </c:forEach>

                <!-- 다음 버튼 -->
                <c:choose>
                    <c:when test="${currentPage < totalPages - 1}">
                        <a href="?page=${currentPage + 1}&size=${pageSize}" class="page-link">다음</a>
                    </c:when>
                    <c:otherwise>
                        <span class="page-link disabled">다음</span>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
</main>
<script src="/static/js/admin.js"></script>
</body>
</html> 