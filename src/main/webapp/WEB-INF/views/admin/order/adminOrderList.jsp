<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>주문관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/adminOrder.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <h2>주문 목록</h2>
        
        <!-- 메시지 표시 -->
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <table class="admin-table">
            <thead>
                <tr>
                    <th>주문번호</th>
                    <th>주문자</th>
                    <th>상품명</th>
                    <th>수량</th>
                    <th>총금액</th>
                    <th>주문일시</th>
                    <th>운송장번호</th>
                    <th>상태</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/kmw/order/detail?orderId=${order.id}" 
                               class="order-number-link" 
                               title="주문 상세 보기">
                                ${order.orderNumber}
                            </a>
                        </td>
                        <td>${order.member.memberName}</td>
                        <td>
                            <c:forEach var="item" items="${order.orderItems}" varStatus="status">
                                <c:if test="${status.first}">
                                    ${item.product.productName}
                                </c:if>
                            </c:forEach>
                            <c:if test="${fn:length(order.orderItems) > 1}">
                                외 ${fn:length(order.orderItems) - 1}건
                            </c:if>
                        </td>
                        <td>
                            <c:set var="totalQty" value="0"/>
                            <c:forEach var="item" items="${order.orderItems}">
                                <c:set var="totalQty" value="${totalQty + item.quantity}"/>
                            </c:forEach>
                            ${totalQty}
                        </td>
                        <td><fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>원</td>
                        <td><fmt:parseDate value="${order.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td>
                            <c:out value="${order.trackingNumber}" default="-"/>
                        </td>
                        <td>
                            <span class="order-status" data-status="${order.orderStatus}">
                                <c:choose>
                                    <c:when test="${order.orderStatus == 0}">상품준비중</c:when>
                                    <c:when test="${order.orderStatus == 1}">배송중</c:when>
                                    <c:when test="${order.orderStatus == 2}">배송완료</c:when>
                                    <c:when test="${order.orderStatus == 3}">구매확정</c:when>
                                    <c:when test="${order.orderStatus == 6}">반품 처리 중</c:when>
                                    <c:when test="${order.orderStatus == 7}">반품 완료</c:when>
                                    <c:when test="${order.orderStatus == 8}">취소 처리 중</c:when>
                                    <c:when test="${order.orderStatus == 9}">주문취소</c:when>
                                    <c:otherwise>기타</c:otherwise>
                                </c:choose>
                            </span>
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
    <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/adminOrder.js"></script>
</main>
</body>
</html>
