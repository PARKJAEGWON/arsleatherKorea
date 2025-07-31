<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>결제하기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/order.css">
    <script>
        // 전역 변수 선언
        const contextPath = '${pageContext.request.contextPath}';
        const memberName = '${member.memberName}';
        const memberPhone = '${member.memberPhone}';
        const memberZipCode = '${member.memberZipCode}';
        const memberAddress1 = '${member.memberAddress1}';
        const memberAddress2 = '${member.memberAddress2}';
    </script>
    <script src="https://js.tosspayments.com/v1"></script>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/order.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />
    <div class="order-flex-wrapper">
        <h1 class="order-title">결제하기</h1>
        
        <!-- 수정된 form 부분 -->
        <form id="orderForm" method="post" onsubmit="event.preventDefault(); handlePayment();">
            <!-- 주문 타입 구분용 hidden input -->
            <c:if test="${not empty cartItems}">
                <input type="hidden" name="orderType" value="cart">
                <c:forEach items="${cartItems}" var="cart" varStatus="status">
                    <input type="hidden" name="cartItems[${status.index}].id" value="${cart.id}">
                    <input type="hidden" name="cartItems[${status.index}].quantity" value="${cart.quantity}">
                </c:forEach>
            </c:if>
            <c:if test="${not empty product}">
                <input type="hidden" name="orderType" value="direct">
                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="quantity" value="${quantity}">
            </c:if>

            <div class="order-main">
                <!-- 주문 상품 정보 -->
                <div class="order-items">
                    <h2>주문 상품 정보</h2>
                    <c:if test="${not empty cartItems}">
                        <c:forEach items="${cartItems}" var="cart">
                            <div class="order-item">
                                <img src="${cart.product.mainImageUrl}" alt="상품 이미지">
                                <div class="item-info">
                                    <h3>${cart.product.productName}</h3>
                                    <p><fmt:formatNumber value="${cart.product.productPrice}" pattern="#,###"/>원</p>
                                    <p>수량: ${cart.quantity}개</p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${not empty product}">
                        <div class="order-item">
                            <img src="${product.mainImageUrl}" alt="상품 이미지">
                            <div class="item-info">
                                <h3>${product.productName}</h3>
                                <p><fmt:formatNumber value="${product.productPrice}" pattern="#,###"/>원</p>
                                <p>수량: ${quantity}개</p>
                            </div>
                        </div>
                    </c:if>
                    <div class="delivery-fee">배송비 <b>무료</b></div>
                </div>

                <!-- 배송 정보 -->
                <div class="order-box">
                    <h2>배송 정보</h2>
                    <div class="checkbox-row">
                        <input type="checkbox" id="sameAsOrderer" onclick="toggleAddressByOrderer()">
                        <label for="sameAsOrderer">주문자 정보와 동일</label>
                    </div>
                    <div class="form-row">
                        <input type="text" name="receiverName" id="receiverName" placeholder="수령인" required>
                        <input type="tel" name="receiverPhone" id="receiverPhone" placeholder="연락처" required>
                    </div>
                    <div class="address-row">
                        <input type="text" name="zipCode" id="zipCode" placeholder="우편번호" required>
                        <button type="button" class="address-search-btn" onclick="searchAddress()">주소찾기</button>
                    </div>
                    <input type="text" name="address1" id="address1" placeholder="주소" required>
                    <input type="text" name="address2" id="address2" placeholder="상세주소" required>
                    <select name="deliveryRequest">
                        <option value="">배송메모를 선택해 주세요.</option>
                        <option value="문 앞에 놓아주세요">문 앞에 놓아주세요</option>
                        <option value="경비실에 맡겨주세요">경비실에 맡겨주세요</option>
                        <option value="직접 수령하겠습니다">직접 수령하겠습니다</option>
                    </select>
                </div>
            </div>

            <!-- 결제 정보 -->
            <div class="order-side">
                <div class="order-box">
                    <h2>주문 요약</h2>
                    <div class="summary-row">
                        <span>상품가격</span>
                        <span><fmt:formatNumber value="${totalPrice}" pattern="#,###"/>원</span>
                    </div>
                    <div class="summary-row">
                        <span>배송비</span>
                        <span>무료</span>
                    </div>
                    <div class="summary-row total">
                        <span>총 주문금액</span>
                        <span><b><fmt:formatNumber value="${totalPrice}" pattern="#,###"/>원</b></span>
                    </div>
                </div>

                <!-- 결제수단 선택 -->
                <div class="order-box">
                    <h2>결제수단</h2>
                    <div class="pay-radio-group">
                        <label class="gender-radio-label">
                            <input type="radio" name="payMethod" value="card" id="payCard" class="gender-radio" checked>
                            <span class="custom-radio"></span>
                            <span>신용카드</span>
                        </label>
                        <label class="gender-radio-label">
                            <input type="radio" name="payMethod" value="bank" id="payBank" class="gender-radio">
                            <span class="custom-radio"></span>
                            <span>무통장입금</span>
                        </label>
                    </div>
                    <div id="bankInfoArea" style="display:none;">
                        <div class="bank-info">
                            <strong>은행명:</strong> 농협<br>
                            <strong>계좌번호:</strong> 302-1950-4806-21<br>
                            <strong>예금주:</strong> 김민욱
                        </div>
                        <div>
                            입금자명 <input type="text" name="depositorName" placeholder="주문자명">
                        </div>
                    </div>
                </div>

                <!-- 약관 동의 -->
                <div class="order-box">
                    <div class="agree-row">
                        <input type="checkbox" id="agreeAll">
                        <label for="agreeAll">전체 동의</label>
                    </div>
                    <div class="agree-row">
                        <input type="checkbox" id="agree1" required>
                        <label for="agree1">이용약관 동의 <a href="javascript:void(0);" onclick="openTermsPopup('${pageContext.request.contextPath}/terms/ars')">약관보기</a> </label>
                    </div>
                    <div class="agree-row">
                        <input type="checkbox" id="agree2" required>
                        <label for="agree2">전자금융거래 이용약관 동의 <a href="javascript:void(0);" onclick="openTermsPopup('${pageContext.request.contextPath}/terms/finance')">약관보기</a> </label>
                    </div>
                    <button type="submit" class="order-btn">결제하기</button>
                </div>
            </div>
        </form>
    </div>
    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html>