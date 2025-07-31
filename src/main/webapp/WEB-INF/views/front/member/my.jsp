<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>내 정보</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/my.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main>
        <div class="center-container">
            <div class="my-header">
                <span class="welcome"><b>${member.memberName}</b> 님, 환영합니다!</span>
            </div>
            <div class="my-main">
                <div class="my-cards">
                    <a href="${pageContext.request.contextPath}/member/profile" class="my-card">
                        <i class="fa-regular fa-user"></i>
                        <span>내정보</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/cart" class="my-card">
                        <i class="fa-solid fa-cart-shopping"></i>
                        <span>장바구니</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/order/list" class="my-card">
                        <i class="fa-solid fa-credit-card"></i>
                        <span>구매내역</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/customer" class="my-card">
                        <i class="fa-solid fa-phone"></i>
                        <span>고객문의</span>
                    </a>
                    <!-- <a href="#" class="my-card">
                        <i class="fa-solid fa-truck"></i>
                        <span>배송조회</span>
                    </a> -->
                </div>
                <!-- <div class="my-list">
                    <a href="#" class="my-list-item"><i class="fa-regular fa-heart"></i> 찜한상품</a>
                    <a href="#" class="my-list-item"><i class="fa-regular fa-comment-dots"></i> 문의내역</a>
                    <a href="#" class="my-list-item"><i class="fa-regular fa-thumbs-up"></i> 리뷰내역</a>
                </div> -->
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
</body>
</html> 