<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>장바구니 - KMW</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/cart.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main>
        <div class="cart-container">
            <h1 class="cart-title"></h1>
            
            <c:choose>
                <c:when test="${empty cartItems}">
                    <div class="empty-cart">
                        <p>장바구니가 비어있습니다.</p>
                        <a href="${pageContext.request.contextPath}/productPost" class="continue-shopping">쇼핑 계속하기</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="cart-items">
                        <thead>
                            <tr>
                                <th>상품 이미지</th>
                                <th>상품명</th>
                                <th>가격</th>
                                <th>수량</th>
                                <th>합계</th>
                                <th>삭제</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>
                                        <img src="${pageContext.request.contextPath}${item.product.mainImageUrl}" 
                                             alt="${item.product.productName}" 
                                             class="cart-item-image">
                                    </td>
                                    <td>${item.product.productName}</td>
                                    <td><fmt:formatNumber value="${item.product.productPrice}" pattern="#,###"/>원</td>
                                    <td>
                                        <div class="quantity-control">
                                            <button class="quantity-btn" data-cart-id="${item.id}" data-action="decrease">-</button>
                                            <input type="number" class="quantity-input" value="${item.quantity}" 
                                                   min="1" data-cart-id="${item.id}">
                                            <button class="quantity-btn" data-cart-id="${item.id}" data-action="increase">+</button>
                                        </div>
                                    </td>
                                    <td><fmt:formatNumber value="${item.product.productPrice * item.quantity}" pattern="#,###"/>원</td>
                                    <td>
                                        <button class="delete-btn" data-cart-id="${item.id}">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="cart-summary">
                        <div class="total-price">
                            총 주문금액: <fmt:formatNumber value="${totalPrice}" pattern="#,###"/>원
                        </div>
                        <button class="checkout-btn" onclick="location.href='${pageContext.request.contextPath}/order/cart'">
                            주문하기
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
    <script src="${pageContext.request.contextPath}/static/js/cart.js"></script>
</body>
</html>
