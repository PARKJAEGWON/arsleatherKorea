<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>상품 상세</title>
    <link rel="stylesheet" href="/static/css/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    <script src="/static/js/adminProductDetail.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <!-- 상단: 대표 이미지 + 상품 정보 -->
        <div class="product-detail-container">
            <img class="product-main-image" src="${product.mainImageUrl}" alt="대표 이미지">
            <table class="product-info-table">
                <tr><th>일련번호</th><td>${product.productCode}
                    <form action="${pageContext.request.contextPath}/admin/kmw/product/delete" method="post" style="display:inline; margin-left:12px;">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?');" style="background:#d23;color:#fff;padding:4px 14px;border:none;border-radius:4px;cursor:pointer;font-size:0.95em;">삭제</button>
                    </form>
                </td></tr>
                <tr><th>상품명</th><td>${product.productName}</td></tr>
                <tr><th>상품브랜드</th>
                    <td>
                        <c:choose>
                            <c:when test="${product.productBrand == 0}">언브런(UNBLOWN)</c:when>
                            <c:when test="${product.productBrand == 1}">???</c:when>
                            <c:when test="${product.productBrand == 2}">???</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr><th>가격</th><td><b style="color:#d23;"><fmt:formatNumber value="${product.productPrice}" pattern="#,###"/>원</b></td></tr>
                <tr><th>카테고리</th>
                    <td>
                        <c:choose>
                            <c:when test="${product.productCategory == 0}">브리프케이스</c:when>
                            <c:when test="${product.productCategory == 1}">토트백</c:when>
                            <c:when test="${product.productCategory == 2}">백팩</c:when>
                            <c:when test="${product.productCategory == 3}">핸드백</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr><th>색상</th><td>${product.productColor}</td></tr>
                <tr><th>소재</th><td>${product.productMaterial}</td></tr>
                <tr><th>성별</th>
                    <td>
                        <c:choose>
                            <c:when test="${product.productGender == 0}">공용</c:when>
                            <c:when test="${product.productGender == 1}">남성</c:when>
                            <c:when test="${product.productGender == 2}">여성</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr><th>시즌 상품</th><td>${product.productIsSeasonal ? '예' : '아니오'}</td></tr>
                <tr><th>상품 상태</th>
                    <td>
                        <c:choose>
                            <c:when test="${product.productStatus == 0}">판매 중</c:when>
                            <c:when test="${product.productStatus == 8}">숨기기</c:when>
                            <c:when test="${product.productStatus == 9}">품절</c:when>
                            <c:otherwise>기타</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </div>

        <!-- 탭 버튼 -->
        <div class="product-detail-tabs">
            <button type="button" class="tab-btn active" onclick="showTab('images')">상세 이미지</button>
            <button type="button" class="tab-btn" onclick="showTab('desc')">상세 설명</button>
            <button type="button" class="tab-btn" onclick="showTab('edit')">상품 수정</button>
        </div>

        <!-- 탭 내용 -->
        <div id="tab-images" class="tab-content">
            <div class="product-detail-images">
                <c:forEach var="img" items="${product.detailImageUrls != null ? product.detailImageUrls.split(',') : null}">
                    <img src="${img}" alt="상세 이미지">
                </c:forEach>
            </div>
        </div>
        <div id="tab-desc" class="tab-content" style="display:none;">
            <div class="product-description">
                <c:out value="${product.productDescription}" escapeXml="false"/>
            </div>
        </div>
        
        <!-- 수정 폼 탭 -->
        <div id="tab-edit" class="tab-content" style="display:none;">
            <form action="${pageContext.request.contextPath}/admin/kmw/product/update" method="post" id="productUpdateForm">
                <input type="hidden" name="productId" value="${product.id}">
                
                <div class="form-group">
                    <label for="productName">상품명</label>
                    <input type="text" id="productName" name="productName" value="${product.productName}" required>
                </div>
                
                <div class="form-group">
                    <label for="productPrice">가격</label>
                    <input type="number" id="productPrice" name="productPrice" value="${product.productPrice}" required>
                </div>
                
                <div class="form-group">
                    <label for="productBrand">상품브랜드</label>
                    <select id="productBrand" name="productBrand" required>
                        <option value="0" ${product.productCategory == 0 ? 'selected' : ''}>언브런(UNBLOWN)</option>
                        <!-- <option value="1" ${product.productCategory == 1 ? 'selected' : ''}>백팩</option>
                        <option value="2" ${product.productCategory == 2 ? 'selected' : ''}>핸드백</option> -->
                    </select>
                </div>

                <div class="form-group">
                    <label for="productCategory">카테고리</label>
                    <select id="productCategory" name="productCategory" required>
                        <option value="0" ${product.productCategory == 0 ? 'selected' : ''}>브리프케이스</option>
                        <option value="1" ${product.productCategory == 1 ? 'selected' : ''}>토트백</option>
                        <option value="2" ${product.productCategory == 2 ? 'selected' : ''}>백팩</option>
                        <option value="2" ${product.productCategory == 3 ? 'selected' : ''}>핸드백</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="productColor">색상</label>
                    <input type="text" id="productColor" name="productColor" value="${product.productColor}" required>
                </div>
                
                <div class="form-group">
                    <label for="productMaterial">소재</label>
                    <input type="text" id="productMaterial" name="productMaterial" value="${product.productMaterial}" required>
                </div>
                
                <div class="form-group">
                    <label for="productGender">성별</label>
                    <select id="productGender" name="productGender" required>
                        <option value="0" ${product.productGender == 0 ? 'selected' : ''}>공용</option>
                        <option value="1" ${product.productGender == 1 ? 'selected' : ''}>남성</option>
                        <option value="2" ${product.productGender == 2 ? 'selected' : ''}>여성</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="productIsSeasonal">시즌 상품</label>
                    <select id="productIsSeasonal" name="productIsSeasonal" required>
                        <option value="true" ${product.productIsSeasonal ? 'selected' : ''}>예</option>
                        <option value="false" ${!product.productIsSeasonal ? 'selected' : ''}>아니오</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="productStatus">상품 상태</label>
                    <select id="productStatus" name="productStatus" required>
                        <option value="0" ${product.productStatus == 0 ? 'selected' : ''}>판매중</option>
                        <option value="8" ${product.productStatus == 8 ? 'selected' : ''}>숨기기</option>
                        <option value="9" ${product.productStatus == 9 ? 'selected' : ''}>품절</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="productDescription">상품 설명</label>
                    <textarea id="productDescription" name="productDescription" rows="4" required>${product.productDescription}</textarea>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="update-btn">수정</button>
                </div>
            </form>
        </div>

        <!-- 에러 메시지 표시 -->
        <c:if test="${not empty error}">
            <div class="error-message">
                ${error}
            </div>
        </c:if>
    </div>
</main>
<script src="/static/js/admin.js"></script>
</body>
</html> 