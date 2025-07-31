<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/orderDetail.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />
    <main>
        <div class="order-detail-wrapper">
            <div class="order-detail-header">
                <span class="order-status<c:if test='${order.orderStatus == 9}'> cancel</c:if>">
                    <c:choose>
                        <c:when test="${order.orderStatus == 0}">상품준비중</c:when>
                        <c:when test="${order.orderStatus == 1}">배송중</c:when>
                        <c:when test="${order.orderStatus == 2}">배송완료</c:when>
                        <c:when test="${order.orderStatus == 9}">주문취소</c:when>
                        <c:otherwise>기타</c:otherwise>
                    </c:choose>
                </span>
                <span class="order-date">${order.createDateTime} 주문</span>
                <span class="order-number">주문번호: ${order.orderNumber}</span>
            </div>
            <div class="order-detail-shipping">
                <div><strong>받는사람</strong> ${order.receiverName} / ${order.receiverPhone}</div>
                <div><strong>주소</strong> [${order.zipCode}] ${order.address1} ${order.address2}</div>
                <div><strong>배송메모</strong> ${order.deliveryRequest}</div>
                <div><strong>운송장번호</strong> <span class="tracking-number">${order.trackingNumber}</span></div>
            </div>
            <div class="order-detail-items">
                <c:forEach var="item" items="${order.orderItems}">
                    <div class="order-detail-item">
                        <img src="${item.product.mainImageUrl}" class="order-detail-thumb" alt="상품이미지">
                        <div class="order-detail-info">
                            <div class="order-detail-title">${item.product.productName}</div>
                            <div class="order-detail-qty">수량: ${item.quantity}개</div>
                        </div>
                        <div class="order-detail-price">
                            <fmt:formatNumber value="${item.totalPrice}" pattern="#,###"/>원
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="order-detail-payment">
                총 결제금액 <span><fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>원</span>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html> 