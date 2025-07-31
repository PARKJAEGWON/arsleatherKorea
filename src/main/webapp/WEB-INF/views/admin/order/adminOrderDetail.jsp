<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>주문 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/adminOrder.css">   
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <div class="page-header">
            <h2>주문 상세</h2>
            <a href="${pageContext.request.contextPath}/admin/kmw/order" class="admin-btn">목록으로</a>
        </div>

        <!-- 주문 기본 정보 -->
        <div class="detail-section">
            <h3>주문 정보</h3>
            <table class="detail-table">
                <tr>
                    <th>주문번호</th>
                    <td>${order.orderNumber}</td>
                    <th>주문일시</th>
                    <td><fmt:parseDate value="${order.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                        <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                </tr>
                <tr>
                    <th>주문상태</th>
                    <td>
                        <select onchange="changeStatus('${order.id}', this.value)" class="admin-select order-status" data-status="${order.orderStatus}">
                            <option value="0" ${order.orderStatus == 0 ? 'selected' : ''}>상품준비중</option>
                            <option value="1" ${order.orderStatus == 1 ? 'selected' : ''}>배송중</option>
                            <option value="2" ${order.orderStatus == 2 ? 'selected' : ''}>배송완료</option>
                            <option value="3" ${order.orderStatus == 3 ? 'selected' : ''}>구매확정</option>
                            <option value="6" ${order.orderStatus == 6 ? 'selected' : ''}>반품 처리 중</option>
                            <option value="7" ${order.orderStatus == 7 ? 'selected' : ''}>반품 완료</option>
                            <option value="8" ${order.orderStatus == 8 ? 'selected' : ''}>취소 처리 중</option>
                            <option value="9" ${order.orderStatus == 9 ? 'selected' : ''}>주문 취소</option>
                        </select>
                    </td>
                    <th>운송장번호</th>
                    <td>
                        <c:if test="${order.orderStatus == 0}">
                            <input type="text" id="trackingNumber_${order.id}" 
                                   value="${order.trackingNumber}" 
                                   placeholder="운송장번호"
                                   style="width: 200px; margin-right: 5px;">
                            <button onclick="saveTrackingNumber('${order.id}')" class="admin-btn" style="padding: 2px 8px;">저장</button>
                        </c:if>
                        <c:if test="${order.orderStatus != 0}">
                            ${order.trackingNumber}
                        </c:if>
                    </td>
                </tr>
            </table>
        </div>

        <!-- 주문자 정보 -->
        <div class="detail-section">
            <h3>주문자 정보</h3>
            <table class="detail-table">
                <tr>
                    <th>이름</th>
                    <td>${order.member.memberName}</td>
                    <th>이메일</th>
                    <td>${order.member.memberEmail}</td>
                </tr>
                <tr>
                    <th>연락처</th>
                    <td>${order.member.memberPhone}</td>
                    <th>회원상태</th>
                    <td>
                        <c:choose>
                            <c:when test="${order.member.memberStatus == 0}">이용중</c:when>
                            <c:when test="${order.member.memberStatus == 8}">회원탈퇴</c:when>
                            <c:when test="${order.member.memberStatus == 9}">정지</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>결제 방식</th>
                    <td>
                        <c:choose>
                            <c:when test="${not empty order.depositor_name}">무통장입금</c:when>
                            <c:otherwise>카드결제</c:otherwise>
                        </c:choose>
                    </td>
                    <th>입금자명</th>
                    <td>
                        <c:if test="${not empty order.depositor_name}">
                            ${order.depositor_name}
                        </c:if>
                    </td>
                </tr>
            </table>
        </div>

        <!-- 배송 정보 -->
        <div class="detail-section">
            <h3>배송 정보</h3>
            <table class="detail-table">
                <tr>
                    <th>수령인</th>
                    <td>${order.receiverName}</td>
                    <th>연락처</th>
                    <td>${order.receiverPhone}</td>
                </tr>
                <tr>
                    <th>배송지</th>
                    <td colspan="3">
                        [${order.zipCode}] ${order.address1} ${order.address2}
                    </td>
                </tr>
                <tr>
                    <th>배송메모</th>
                    <td colspan="3">${order.deliveryRequest}</td>
                </tr>
            </table>
        </div>

        <!-- 주문 상품 정보 -->
        <div class="detail-order-section">
            <h3>주문 상품</h3>
            <table class="detail-table">
                <thead>
                    <tr>
                        <th>상품 이미지</th>
                        <th>상품명</th>
                        <th>상품일련번호</th>
                        <th>수량</th>
                        <th>가격</th>
                        <th>합계</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.orderItems}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}${item.product.mainImageUrl}" 
                                     style="width: 50px; height: 50px; object-fit: cover;">
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/kmw/product/detail?id=${item.product.id}" 
                                   class="product-link">
                                    ${item.product.productName}
                                </a>
                            </td>
                            <td>${item.product.productCode}</td>
                            <td>${item.quantity}</td>
                            <td><fmt:formatNumber value="${item.price}" pattern="#,###"/>원</td>
                            <td><fmt:formatNumber value="${item.totalPrice}" pattern="#,###"/>원</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="5">총 주문금액</th>
                        <td><fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>원</td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/adminOrder.js"></script>
</main>
</body>
</html> 