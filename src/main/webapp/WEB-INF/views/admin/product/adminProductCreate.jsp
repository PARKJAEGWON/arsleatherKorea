<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>상품 등록</title>
    <link rel="stylesheet" href="/static/css/admin.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <h2>상품 등록</h2>
        <form action="${pageContext.request.contextPath}/admin/kmw/product/create" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label>상품 코드</label>
                <input type="text" name="productCode" required>
            </div>
            <div class="form-group">
                <label>상품명</label>
                <input type="text" name="productName" required>
            </div>
            <div class="form-group">
                <label>상품 브랜드</label>
                <select name="productBrand" required>
                    <option value="0">언브런(UNBLOWN)</option>
                    <!-- <option value="1">백팩</option>
                    <option value="2">핸드백</option> -->
                </select>
            </div>
            <div class="form-group">
                <label>카테고리</label>
                <select name="productCategory" required>
                    <option value="0">브리프케이스</option>
                    <option value="1">토트백</option>
                    <option value="2">백팩</option>
                    <option value="3">핸드백</option>
                </select>
            </div>
            <div class="form-group">
                <label>성별</label>
                <select name="productGender" required>
                    <option value="0">공용</option>
                    <option value="1">남성</option>
                    <option value="2">여성</option>
                </select>
            </div>
            <div class="form-group">
                <label>시즌 상품</label>
                <select name="productIsSeasonal" required>
                    <option value="true">예</option>
                    <option value="false">아니요</option>
                </select>
            </div>
            <div class="form-group">
                <label>소재</label>
                <input type="text" name="productMaterial" required>
            </div>
            <div class="form-group">
                <label>색상</label>
                <input type="text" name="productColor" required>
            </div>
            <!-- <div class="form-group">
                <label>재고</label>
                <input type="number" name="productStock" required>
            </div> -->
            <div class="form-group">
                <label>가격</label>
                <input type="number" name="productPrice" required>
            </div>
            <div class="form-group">
                <label>상세설명</label>
                <textarea name="productDescription" rows="4" required></textarea>
            </div>
            <div class="form-group">
                <label>대표 이미지</label>
                <input type="file" name="mainImage" accept="image/*" required>
            </div>
            <div class="form-group">
                <label>상세 이미지</label>
                <input type="file" name="detailImages" accept="image/*" multiple>
            </div>
            <div class="button-group">
                <button type="submit">등록</button>
                <button type="button" onclick="location.href='${pageContext.request.contextPath}/admin/kmw/product'">취소</button>
            </div>
        </form>
    </div>
</main>
<script src="/static/js/admin.js"></script>
</body>
</html> 