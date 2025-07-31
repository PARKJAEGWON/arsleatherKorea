<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원 상세 정보</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/adminOrder.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <div class="page-header">
            <h2>회원 상세 정보</h2>
            <a href="${pageContext.request.contextPath}/admin/kmw/member" class="admin-btn">목록으로</a>
        </div>

        <!-- 회원 기본 정보 섹션 -->
        <div class="detail-section">
            <h3>기본 정보</h3>
            <table class="detail-table">
                <tr>
                    <th>이름</th>
                    <td>${member.memberName}</td>
                    <th>회원상태</th>
                    <td>
                        <c:choose>
                        <c:when test="${member.memberStatus == 0}">이용중</c:when>
                        <c:when test="${member.memberStatus == 8}">탈퇴</c:when>
                        <c:when test="${member.memberStatus == 9}">정지</c:when>
                        <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>아이디</th>
                    <td>${member.memberLoginId}</td>
                    <th>이메일</th>
                    <td>${member.memberEmail}</td>
                </tr>
                <tr>
                    <th>연락처</th>
                    <td>${member.memberPhone}</td>
                    <th>성별</th>
                    <td>
                        <c:choose>
                            <c:when test="${member.memberGender == 'MALE'}">남</c:when>
                            <c:when test="${member.memberGender == 'FEMALE'}">여</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>생년월일</th>
                    <td>${member.memberBirthDate}</td>
                    <th>가입일</th>
                    <td>${member.createDateTime.toLocalDate()}</td>
                </tr>
                <tr>
                    <!-- <th>회원상태</th>
                    <td colspan="3">
                        <select onchange="changeMemberStatus('${member.id}', this.value)" class="admin-select">
                            <option value="0" ${member.memberStatus == 0 ? 'selected' : ''}>이용중</option>
                            <option value="8" ${member.memberStatus == 8 ? 'selected' : ''}>탈퇴</option>
                            <option value="9" ${member.memberStatus == 9 ? 'selected' : ''}>정지</option>
                        </select>
                    </td> -->
                </tr>
            </table>
        </div>

        <!-- 회원 메모 섹션 -->
        <div class="detail-section">
            <h3>회원 메모</h3>
            <div class="member-description-area">
                <div class="description-display" id="descriptionDisplay">
                    <p>${empty member.memberDescription ? '등록된 메모가 없습니다.' : member.memberDescription}</p>
                    <button type="button" class="btn" onclick="showDescriptionEdit()">수정</button>
                </div>
                <div class="description-edit" id="descriptionEdit" style="display: none;">
                    <textarea id="memberDescription" class="admin-textarea" rows="4">${member.memberDescription}</textarea>
                    <div class="button-group">
                        <button type="button" class="btn" onclick="saveMemberDescription(this)" data-member-id="${member.id}">저장</button>
                        <button type="button" class="btn" onclick="cancelDescriptionEdit()">취소</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 회원 주소 정보 섹션 -->
        <div class="detail-section">
            <h3>주소 정보</h3>
            <table class="detail-table">
                <tr>
                    <th>우편번호</th>
                    <td>${member.memberZipCode}</td>
                </tr>
                <tr>
                    <th>기본주소</th>
                    <td colspan="3">${member.memberAddress1}</td>
                </tr>
                <tr>
                    <th>상세주소</th>
                    <td colspan="3">${member.memberAddress2}</td>
                </tr>
            </table>
        </div>

        <!-- 회원 동의 정보 섹션 -->
        <div class="detail-section">
            <h3>동의 정보</h3>
            <table class="detail-table">
                <tr>
                    <th>이메일 수신 동의</th>
                    <td>
                        <c:choose>
                            <c:when test="${member.memberEmailAgree}">동의</c:when>
                            <c:otherwise>미동의</c:otherwise>
                        </c:choose>
                    </td>
                    <th>동의일시</th>
                    <td>
                        <c:if test="${not empty member.memberEmailAgreeTime}">
                            <fmt:parseDate value="${member.memberEmailAgreeTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedEmailDate" />
                            <fmt:formatDate value="${parsedEmailDate}" pattern="yyyy-MM-dd HH:mm"/>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <th>SMS 수신 동의</th>
                    <td>
                        <c:choose>
                            <c:when test="${member.memberSmsAgree}">동의</c:when>
                            <c:otherwise>미동의</c:otherwise>
                        </c:choose>
                    </td>
                    <th>동의일시</th>
                    <td>
                        <c:if test="${not empty member.memberSmsAgreeTime}">
                            <fmt:parseDate value="${member.memberSmsAgreeTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedSmsDate" />
                            <fmt:formatDate value="${parsedSmsDate}" pattern="yyyy-MM-dd HH:mm"/>
                        </c:if>
                    </td>
                </tr>
            </table>
        </div>

        <!-- 구매 내역 섹션 -->
        <div class="detail-order-section">
            <h3>구매 내역</h3>
            <table class="detail-table">
                <thead>
                    <tr>
                        <th>주문번호</th>
                        <th>주문일시</th>
                        <th>상품명</th>
                        <th>수량</th>
                        <th>총금액</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/kmw/order/detail?orderId=${order.id}" 
                                   class="order-number-link">
                                    ${order.orderNumber}
                                </a>
                            </td>
                            <td>
                                <fmt:parseDate value="${order.createDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
                                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
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
                            <td>
                                <c:choose>
                                    <c:when test="${order.orderStatus == 0}">상품준비중</c:when>
                                    <c:when test="${order.orderStatus == 1}">배송중</c:when>
                                    <c:when test="${order.orderStatus == 2}">배송완료</c:when>
                                    <c:when test="${order.orderStatus == 3}">구매확정</c:when>
                                    <c:when test="${order.orderStatus == 9}"><span class="order-cancel-status">주문취소</span></c:when>
                                    <c:otherwise>기타</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty orders}">
                        <tr>
                            <td colspan="6">구매 내역이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
    <script src="/static/js/admin.js"></script>
</main>
</body>
</html> 