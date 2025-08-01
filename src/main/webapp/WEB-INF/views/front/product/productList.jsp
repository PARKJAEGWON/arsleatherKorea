<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Collection - Luxury Fashion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/product.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main>
        <section class="collection-header">
            <h1 class="collection-title">Collection</h1>
            <!-- <p class="collection-description">럭셔리한 가죽 제품들의 컬렉션을 만나보세요.</p> -->
        </section>

        <div class="product-layout">
            <div class="category-menu">
                <div class="brand-section">
                    <h3>카테고리</h3>
                    <ul class="brand-list">
                        <li class="brand-item">
                            <span class="brand-title">
                                <a href="?brand=0" class="${param.brand == '0' ? 'active' : ''}">UNBLOWN</a>
                            </span>
                            <ul class="category-sublist">
                                <li><a href="?category=0" class="${param.category == '0' ? 'active' : ''}">브리프케이스</a></li>
                                <li><a href="?category=1" class="${param.category == '1' ? 'active' : ''}">토트백</a></li>
                                <li><a href="?category=2" class="${param.category == '2' ? 'active' : ''}">백팩</a></li>
                                <li><a href="?category=3" class="${param.category == '3' ? 'active' : ''}">핸드백</a></li>
                            </ul>
                        </li>
                        <!-- <li class="brand-item">
                            <span class="brand-title">다른 브랜드</span>
                            <ul class="category-sublist">
                                <li><a href="?category=0&brand=unbroken" class="${param.category == '0' && param.brand == 'unbroken' ? 'active' : ''}">서류가방</a></li>
                                <li><a href="?category=1&brand=unbroken" class="${param.category == '1' && param.brand == 'unbroken' ? 'active' : ''}">백팩</a></li>
                                <li><a href="?category=2&brand=unbroken" class="${param.category == '2' && param.brand == 'unbroken' ? 'active' : ''}">핸드백</a></li>
                            </ul>
                        </li>
                        <li class="brand-item">
                            <span class="brand-title">다른 브랜드</span>
                            <ul class="category-sublist">
                                <li><a href="?category=0&brand=landik" class="${param.category == '0' && param.brand == 'landik' ? 'active' : ''}">서류가방</a></li>
                                <li><a href="?category=1&brand=landik" class="${param.category == '1' && param.brand == 'landik' ? 'active' : ''}">백팩</a></li>
                                <li><a href="?category=2&brand=landik" class="${param.category == '2' && param.brand == 'landik' ? 'active' : ''}">핸드백</a></li>
                            </ul>
                        </li>
                    </ul> -->
                </div>
            </div>

            <div class="product-grid">
                <c:forEach var="product" items="${productPage.content}">
                    <c:if test="${product.productStatus != 8}">
                        <a href="${pageContext.request.contextPath}/productPost/detail?id=${product.id}" class="product-card">
                            <img src="${pageContext.request.contextPath}${product.mainImageUrl}" alt="${product.productName}" class="product-image">
                            <div class="product-info">
                                <h3 class="product-name">${product.productName}</h3>
                                <div class="product-price">
                                    <fmt:formatNumber value="${product.productPrice}" type="number" />원
                                    <c:if test="${product.productStatus == 9}">
                                        <span class="sold-out-badge">품절</span>
                                    </c:if>
                                </div>
                            </div>
                        </a>
                    </c:if>
                </c:forEach>
            </div>
        </div>

        <div class="pagination">
            <c:if test="${totalPages > 0}">
                <c:if test="${currentPage > 0}">
                    <a href="?page=${currentPage-1}${not empty param.category ? '&category='.concat(param.category) : ''}${not empty param.brand ? '&brand='.concat(param.brand) : ''}">&lt;</a>
                </c:if>
                
                <c:forEach begin="0" end="${totalPages-1}" var="i">
                    <a href="?page=${i}${not empty param.category ? '&category='.concat(param.category) : ''}${not empty param.brand ? '&brand='.concat(param.brand) : ''}" class="${currentPage == i ? 'active' : ''}">${i+1}</a>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages-1}">
                    <a href="?page=${currentPage+1}${not empty param.category ? '&category='.concat(param.category) : ''}${not empty param.brand ? '&brand='.concat(param.brand) : ''}">&gt;</a>
                </c:if>
            </c:if>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html> 