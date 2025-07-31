<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 내역</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/orderList.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/front/common/header.jsp" />
<div class="order-list-wrapper">
    <h1 style="font-size:24px;font-weight:bold;margin-bottom:30px;color:#333;">주문 내역</h1>
    <c:choose>
        <c:when test="${empty orderList}">
            <div class="empty-order-list" style="text-align:center;padding:50px;background:#f8f9fa;border-radius:8px;color:#666;">주문 내역이 없습니다.</div>
        </c:when>
        <c:otherwise>
            <c:forEach var="order" items="${orderList}">
                <div class="order-naver-card">
                    <div class="order-card-header">
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
                        <!-- <button class="order-close-btn">×</button> -->
                    </div>
                    <div class="order-card-body">
                        <img src="<c:out value='${order.orderItems[0].product.mainImageUrl}'/>" class="order-thumb" alt="상품이미지">
                        <div class="order-info">
                            <div class="order-date">${order.createDateTime} 주문</div>
                            <div class="order-title">
                                ${order.orderItems[0].product.productName}
                                <c:if test="${fn:length(order.orderItems) > 1}">
                                    외 ${fn:length(order.orderItems) - 1}건
                                </c:if>
                            </div>
                            <div class="order-price">
                                <span class="price"><fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>원</span>
                            </div>
                            <a href="/order/detail?orderId=${order.id}" class="order-detail-link">상세보기 &gt;</a>
                        </div>
                    </div>
                    <div class="order-card-footer">
                        <c:choose>
                            <c:when test="${order.orderStatus == 0 || order.orderStatus == '0'}">
                                <button class="order-btn" onclick="cancelOrder('${order.id}')">주문 취소</button>
                            </c:when>
                            <c:when test="${order.orderStatus == 1 || order.orderStatus == '1'}">
                                <button class="order-btn" onclick="returnOrder('${order.id}')">반품신청</button>
                            </c:when>
                            <c:when test="${order.orderStatus == 2 || order.orderStatus == '2'}">
                                <button class="order-btn" onclick="returnOrder('${order.id}')">반품신청</button>
                            </c:when>
                        </c:choose>
                        <!-- <button class="order-btn">영수증조회</button> -->
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
<jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
<script src="${pageContext.request.contextPath}/static/js/orderList.js"></script>
</body>
</html> 