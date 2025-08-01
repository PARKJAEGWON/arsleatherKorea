<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/signup.css">

    <!-- <link href="https://fonts.googleapis.com/css2?family=Allura&display=swap" rel="stylesheet"> -->
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />
    <main>
        <div class="signup-container">
            <h2 class="signup-title">회원가입</h2>
            <% if(request.getAttribute("signupError") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("signupError") %>
                </div>
            <% } %>
            <form class="signup-form" method="post" action="${pageContext.request.contextPath}/member/signup">
                <div class="input-group">
                    <input type="text" name="memberLoginId" placeholder="아이디" required>
                </div>
                <div class="input-group">
                    <input type="password" name="memberPassword" placeholder="비밀번호" required>
                </div>
                <div class="input-group">
                    <input type="text" name="memberName" placeholder="이름" required>
                </div>
                <div class="input-group">
                    <input type="date" name="memberBirthDate" placeholder="생년월일" required>
                </div>
                <div class="input-group gender-group">
                    <label class="gender-radio-label">
                        <span class="gender-text">MALE</span>
                        <input type="radio" name="memberGender" value="MALE" class="gender-radio">
                        <span class="custom-radio"></span>
                    </label>
                    <label class="gender-radio-label">
                        <span class="gender-text">FEMALE</span>
                        <input type="radio" name="memberGender" value="FEMALE" class="gender-radio">
                        <span class="custom-radio"></span>
                    </label>
                </div>
                <div class="input-group phone-group">
                    <input type="text" name="memberPhone" placeholder="휴대폰 번호" required>
                    <!-- <button type="button" class="phone-verify-btn">인증번호 발송</button> -->
                </div>
                <!-- <div class="input-group">
                    <input type="text" name="phoneVerifyCode" placeholder="인증번호 입력">
                </div> -->
                <div class="input-group">
                    <input type="email" name="memberEmail" placeholder="이메일" required>
                </div>
                <div class="input-group zip-group">
                    <input type="text" name="memberZipCode" placeholder="우편번호" style="width:60%">
                    <button type="button" class="zip-search-btn">우편번호 찾기</button>
                </div>
                <div class="input-group">
                    <input type="text" name="memberAddress1" placeholder="주소" required>
                </div>
                <div class="input-group">
                    <input type="text" name="memberAddress2" placeholder="상세주소">
                </div>
                <div class="agree-group">
                    <label><input type="checkbox" name="memberEmailAgree"> 이메일 수신 동의</label>
                    <label><input type="checkbox" name="memberSmsAgree"> SMS 수신 동의</label>
                    <label><input type="checkbox" id="termsAgree" required> ARS LEATHER 이용약관 동의 <a href="javascript:void(0);" onclick="openTermsPopup('${pageContext.request.contextPath}/terms/ars')">약관보기</a></label>
                    <label><input type="checkbox" id="privacyAgree" required> 개인정보 수집 및 이용약관 동의 <a href="javascript:void(0);" onclick="openTermsPopup('${pageContext.request.contextPath}/terms/privacy')">약관보기</a></label>
                </div>
                <button type="submit" class="signup-btn">회원가입</button>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
    <script src="/static/js/signup.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/terms.js"></script>
</body>
</html> 