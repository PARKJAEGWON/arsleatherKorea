<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ARS LEATHER</title>
    <!-- <%-- 톰캣 내부에서 서버xml이랑 컨테스트xml에서 ${pageContext.request.contextPath} 확인가능 이녀석이 환경에 맞춰 경로를 유동적으로 찾아줌 --%> -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <!-- Swiper CSS CDN -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main>
        <section class="hero-section">
            <div class="swiper hero-swiper">
                <div class="swiper-wrapper">
                    <!-- <div class="swiper-slide">
                        <div class="hero-content">
                            <h1>New Collection</h1>
                            <p>Discover the latest trends</p>
                            <a href="#" class="cta-button">자세히 보기</a>
                        </div>
                    </div> -->
                    <div class="swiper-slide">
                        <img src="${pageContext.request.contextPath}/static/img/slide1.jpg" alt="슬라이드1">
                    </div>
                    <div class="swiper-slide">
                        <img src="${pageContext.request.contextPath}/static/img/slide2.jpg" alt="슬라이드2">
                    </div>
                    <div class="swiper-slide">
                        <img src="${pageContext.request.contextPath}/static/img/slide3.jpg" alt="슬라이드3">
                    </div>
                    <div class="swiper-slide">
                        <img src="${pageContext.request.contextPath}/static/img/slide4.jpg" alt="슬라이드4">
                    </div>
                    <div class="swiper-slide">
                        <img src="${pageContext.request.contextPath}/static/img/slide5.jpg" alt="슬라이드5">
                    </div>
                </div>
                <!-- 페이지네이션(점) -->
                <div class="swiper-pagination"></div>
            </div>
        </section>

        <section class="featured-products-season">
            <h2>SEASON</h2>
            <div class="swiper recommended-swiper">
                <div class="swiper-wrapper">
                    <c:forEach var="product" items="${seasonalProducts}">
                        <div class="swiper-slide">
                            <a href="${pageContext.request.contextPath}/productPost/detail?id=${product.id}" class="product-card">
                                <img src="${pageContext.request.contextPath}${product.mainImageUrl}" alt="${product.productName}" class="product-image">
                            </a>
                        </div>
                    </c:forEach>
                </div>
                <div class="swiper-button-next"></div>
                <div class="swiper-button-prev"></div>
            </div>
        </section>

        <section class="split-section">
            <div class="split-image">
                <img src="${pageContext.request.contextPath}/static/img/men.jpg">
            </div>
            <div class="split-content">
                <section class="featured-products-women">
                    <h2>MEN</h2>
                    <p class="index-product-description">도심의 분주함 속에서도 중심을 잃지 않는 남성을 위해, <br>고요한 힘과 균형감을 담아 설계된 가방입니다. 전통적인 형태에 현대적인 감성을 더해, <br>시간을 초월한 스타일을 제안합니다.</p>
                    <div class="swiper recommended-swiper">
                        <div class="swiper-wrapper">
                            <c:forEach var="product" items="${menProducts}" begin="0" end="2">
                                <div class="swiper-slide">
                                    <a href="${pageContext.request.contextPath}/productPost/detail?id=${product.id}" class="product-card">
                                        <img src="${pageContext.request.contextPath}${product.mainImageUrl}" alt="${product.productName}" class="product-image">
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="swiper-button-next"></div>
                        <div class="swiper-button-prev"></div>
                    </div>
                </section>
            </div>
        </section>

        <section class="split-section">
            <div class="split-content">
                <section class="featured-products-men">
                    <h2>WOMEN</h2>
                    <p class="index-product-description">유려한 곡선과 절제된 디테일이 살아 있는 디자인. <br>실용성과 예술적 감각의 조화 속에서, <br>여성의 태도와 스타일을 완성하는 가방 컬렉션을 제안합니다.</p>
                    <div class="swiper recommended-swiper">
                        <div class="swiper-wrapper">
                            <c:forEach var="product" items="${womenProducts}" begin="0" end="2">
                                <div class="swiper-slide">
                                    <a href="${pageContext.request.contextPath}/productPost/detail?id=${product.id}" class="product-card">
                                        <img src="${pageContext.request.contextPath}${product.mainImageUrl}" alt="${product.productName}" class="product-image">
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </section>
            </div>
            <div class="split-image">
                <img src="${pageContext.request.contextPath}/static/img/women.jpg">
            </div>
        </section>
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <!-- Swiper JS CDN -->
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
</body>
</html> 