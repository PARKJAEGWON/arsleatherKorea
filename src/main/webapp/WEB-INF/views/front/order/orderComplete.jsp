<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 완료</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/order.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />
    <div class="order-complete-wrapper">
        <div class="order-complete-icon">✔</div>
        <h1 class="order-complete-title">주문이 완료되었습니다!</h1>
        <p class="order-complete-desc">주문해 주셔서 감사합니다.<br>주문하신 상품은 빠르게 배송해 드리겠습니다.</p>

        <div class="order-info-card">
            <h2>주문 정보</h2>
            <div class="info-row"><span>주문번호</span><span class="info-value">${order.orderNumber}</span></div>
            <div class="info-row"><span>주문일시</span><span class="info-value">${order.createDateTime}</span></div>
            <div class="info-row"><span>결제금액</span><span class="info-value"><fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>원</span></div>
        </div>

        <div class="order-info-card">
            <h2>배송 정보</h2>
            <div class="info-row"><span>받는 사람</span><span class="info-value">${order.receiverName}</span></div>
            <div class="info-row"><span>연락처</span><span class="info-value">${order.receiverPhone}</span></div>
            <div class="info-row"><span>배송지</span><span class="info-value">${order.address1} ${order.address2}</span></div>
            <c:if test="${not empty order.deliveryRequest}">
                <div class="info-row"><span>배송 요청사항</span><span class="info-value">${order.deliveryRequest}</span></div>
            </c:if>
        </div>

        <div class="order-complete-btns">
            <a href="/" class="btn-main">쇼핑 계속하기</a>
            <a href="/order/list" class="btn-sub">주문 내역 보기</a>
        </div>
    </div>
    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html> 