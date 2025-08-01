<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${product.productName} - Luxury Fashion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/product.css">
    <!-- Swiper CSS CDN -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main>
        <div class="product-detail-container">
            <!-- 왼쪽: 상품 이미지 -->
            <div class="product-images">
                <img src="${product.mainImageUrl}" alt="${product.productName}" class="main-image" id="mainImage">
                <div class="thumbnail-container">
                    <img src="${product.mainImageUrl}" alt="메인 이미지" class="thumbnail active" onclick="changeMainImage(this.src)">
                    <c:forEach var="img" items="${product.detailImageUrls != null ? product.detailImageUrls.split(',') : null}">
                        <img src="${img}" alt="상세 이미지" class="thumbnail" onclick="changeMainImage(this.src)">
                    </c:forEach>
                </div>
            </div>

            <!-- 오른쪽: 상품 정보 및 구매 옵션 -->
            <div class="product-info">
                <input type="hidden" id="productId" value="${product.id}">
                <h1 class="product-title">${product.productName}</h1>
                <div class="product-price"><fmt:formatNumber value="${product.productPrice}" type="number" />원</div>
                
                
                <div class="product-options">
                    <div class="option-group">
                        <label>색상</label>
                        <div class="product-color">${product.productColor}</div>
                    </div>
                    
                    <div class="option-group">
                        <label>수량</label>
                        <div class="quantity-selector">
                            <button class="quantity-btn" onclick="decreaseQuantity()">-</button>
                            <input type="number" class="quantity-input" id="quantity" value="1" min="1" max="10">
                            <button class="quantity-btn" onclick="increaseQuantity()">+</button>
                        </div>
                    </div>
                </div>

                <div class="purchase-buttons">
                    <button class="btn-cart" onclick="addToCart()">장바구니</button>
                    <button class="btn-buy" onclick="buyNow()">구매하기</button>
                </div>

                <div class="product-description">
                    <h2>상품 설명</h2>
                    <div class="description-content">
                        <c:out value="${product.productDescription}" escapeXml="false"/>
                    </div>
                </div>
            </div>
        </div>

        <!-- 탭 버튼 -->
        <div class="product-detail-tabs">
            <button type="button" class="tab-btn active" onclick="showTab('images')">상세 이미지</button>
            <!-- <button type="button" class="tab-btn" onclick="showTab('desc')">상세 설명</button> -->
        </div>

        <!-- 탭 내용 -->
        <div id="tab-images" class="tab-content active">
            <div class="product-detail-images">
                <img src="${product.mainImageUrl}" alt="메인 이미지">
                <c:forEach var="img" items="${product.detailImageUrls != null ? product.detailImageUrls.split(',') : null}">
                    <img src="${img}" alt="상세 이미지">
                </c:forEach>
            </div>
        </div>
        <!-- <div id="tab-desc" class="tab-content">
            <div class="product-description">
                <c:out value="${product.productDescription}" escapeXml="false"/>
            </div>
        </div> -->

        <!-- <div class="product-detail-tabs">
            <button type="button" class="tab-btn active" onclick="showTab('images')">상세 이미지</button>
            <button type="button" class="tab-btn" onclick="showTab('desc')">상세 설명</button>
        </div>

        <div id="tab-images" class="tab-content active">
            <div class="product-detail-images">
                <img src="${product.mainImageUrl}" alt="메인 이미지">
                <c:forEach var="img" items="${product.detailImageUrls != null ? product.detailImageUrls.split(',') : null}">
                    <img src="${img}" alt="상세 이미지">
                </c:forEach>
            </div>
        </div>
        <div id="tab-desc" class="tab-content">
            <div class="product-description">
                <c:out value="${product.productDescription}" escapeXml="false"/>
            </div>
        </div> -->
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
    
    <script>
        const pageContext = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/static/js/product.js"></script>
</body>
</html> 