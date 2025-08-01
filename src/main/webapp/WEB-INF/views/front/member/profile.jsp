<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>내 프로필</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/front/common/header.jsp" />

    <main>
        <div class="center-container">
            <h2 class="profile-title">내 프로필</h2>
            <form id="profileForm">
                <div class="profile-table">
                    <div class="profile-row">
                        <div class="profile-label">성명</div>
                        <div class="profile-value">
                            <span id="nameView">
                                <i class="fa-regular fa-user"></i> 
                                <span id="nameText">${member.memberName}</span> / 
                                <span class="profile-gender" id="genderText">${member.memberGender == 'FEMALE' ? '여성' : '남성'}</span>
                            </span>
                            <span id="nameModify" style="display:none; gap: 8px;">
                                <input type="text" id="nameInput" value="${member.memberName}" style="width:100px;">
                                <select id="genderInput">
                                    <option value="FEMALE" ${member.memberGender == 'FEMALE' ? 'selected' : ''}>여성</option>
                                    <option value="MALE" ${member.memberGender == 'MALE' ? 'selected' : ''}>남성</option>
                                </select>
                            </span>
                        </div>
                    </div>
                    <div class="profile-row">
                        <div class="profile-label">비밀번호</div>
                        <div class="profile-value">
                            <span id="passwordView">
                                <i class="fa-solid fa-lock"></i> 
                                <span>********</span>
                            </span>
                            <span id="passwordModify" style="display:none; gap: 8px;">
                                <input type="password" id="passwordInput" placeholder="변경할 비밀번호" style="width:140px;">
                            </span>
                        </div>
                    </div>
                    <div class="profile-row">
                        <div class="profile-label">이메일</div>
                        <div class="profile-value">
                            <span id="emailView">
                                <i class="fa-regular fa-envelope"></i> 
                                <span id="emailText">${member.memberEmail}</span>
                            </span>
                            <span id="emailModify" style="display:none; gap: 8px;">
                                <input type="email" id="emailInput" value="${member.memberEmail}" style="width:180px;">
                            </span>
                        </div>
                    </div>
                    <div class="profile-row">
                        <div class="profile-label">연락처</div>
                        <div class="profile-value">
                            <span id="phoneView">
                                <i class="fa-solid fa-phone"></i> 
                                <span id="phoneText">${member.memberPhone}</span>
                            </span>
                            <span id="phoneModify" style="display:none; gap: 8px;">
                                <input type="text" id="phoneInput" value="${member.memberPhone}" style="width:140px;">
                            </span>
                        </div>
                    </div>
                    <div class="profile-row">
                        <div class="profile-label">우편번호</div>
                        <div class="profile-value">
                            <span id="addressView">
                                <i class="fa-solid fa-location-dot"></i> 
                                <span id="zipText">${member.memberZipCode}</span><br>
                                <span id="addrText">
                                    ${member.memberAddress1}
                                    <c:if test="${not empty member.memberAddress2}">
                                        ${member.memberAddress2}
                                    </c:if>
                                </span>
                            </span>
                            <span id="addressModify" style="display:none; gap: 8px; flex-direction:column; align-items:flex-start;">
                                <input type="text" id="zipInput" value="${member.memberZipCode}" style="width:80px; margin-bottom:4px;">
                                <button type="button" id="zipSearchBtn">우편번호 찾기</button>
                                <input type="text" id="addrInput" value="${member.memberAddress1}" style="width:220px; margin-bottom:4px;">
                                <input type="text" id="addrDetailInput" value="${member.memberAddress2}" style="width:220px;">
                            </span>
                        </div>
                    </div>
                </div>
                <h2 class="profile-title">선택 정보</h2>
                <div class="profile-table">
                    <div class="profile-row">
                        <div class="profile-label">마케팅 수신 동의</div>
                        <div class="profile-value">
                            <span id="marketingView">
                                <i class="fa-regular fa-comment-dots"></i>
                                <span id="smsText">
                                    SMS
                                    <i class="fa-solid fa-check agree-icon${member.memberSmsAgree ? '' : ' hidden'}"></i>
                                    <i class="fa-solid fa-xmark disagree-icon${member.memberSmsAgree ? ' hidden' : ''}"></i>
                                </span>
                                <i class="fa-regular fa-envelope"></i>
                                <span id="emailAgreeText">
                                    Email
                                    <i class="fa-solid fa-check agree-icon${member.memberEmailAgree ? '' : ' hidden'}"></i>
                                    <i class="fa-solid fa-xmark disagree-icon${member.memberEmailAgree ? ' hidden' : ''}"></i>
                                </span>
                            </span>
                            <span id="marketingModify" style="display:none; gap: 16px;">
                                <label><input type="checkbox" id="smsInput" ${member.memberSmsAgree ? 'checked' : ''}> SMS</label>
                                <label><input type="checkbox" id="emailAgreeInput" ${member.memberEmailAgree ? 'checked' : ''}> Email</label>
                            </span>
                        </div>
                    </div>
                </div>
            </form>
            <div class="profile-btns-flex">
                <div class="profile-btns">
                    <button type="button" class="profile-btn modify" id="modifyBtn">수정하기</button>
                    <button type="button" class="profile-btn" id="saveBtn" style="display:none;">저장</button>
                    <button type="button" class="profile-btn withdraw" id="cancelBtn" style="display:none;">취소</button>
                </div>
                <form id="withdrawForm" method="post" action="${pageContext.request.contextPath}/member/withdraw" style="margin:0; padding:0;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="button" class="profile-btn withdraw" id="withdrawBtn">탈퇴하기</button>
                </form>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/front/common/footer.jsp" />
    <script src="/static/js/profile.js"></script>
</body>
</html>