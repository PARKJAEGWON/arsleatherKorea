<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/terms.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/find.css">
</head>
<body class="terms-popup">
    <h2>비밀번호 찾기</h2>
    <div class="content">
        <!-- 폼 영역 -->
        <div id="searchForm">
            <form action="${pageContext.request.contextPath}/member/findPassword" method="post">
                <div class="input-group">
                    <label for="memberName">이름</label>
                    <input type="text" id="memberName" name="memberName" required>
                </div>
                
                <div class="input-group">
                    <label for="memberLoginId">아이디</label>
                    <input type="text" id="memberLoginId" name="memberLoginId" required>
                </div>

                <div class="input-group">
                    <label for="memberEmail">이메일</label>
                    <input type="email" id="memberEmail" name="memberEmail" required>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="submit-btn">비밀번호 찾기</button>
                </div>
            </form>
        </div>

        <!-- 결과 표시 영역 (비밀번호 재설정 폼) -->
        <div id="resultArea" class="result-area" style="display: none">
            <c:if test="${not empty success}">
                <c:choose>
                    <c:when test="${success && completed}">
                        <!-- 비밀번호 변경 완료 화면 -->
                        <div class="success-message">
                            <p>${message}</p>
                            <button type="button" onclick="window.close()" class="close-btn">창 닫기</button>
                        </div>
                    </c:when>
                    <c:when test="${success}">
                        <!-- 비밀번호 변경 폼 (기존 코드) -->
                        <form action="${pageContext.request.contextPath}/member/resetPassword" method="post">
                            <!-- 이전 단계에서 입력한 아이디를 hidden으로 전달 -->
                            <input type="hidden" name="memberLoginId" value="${memberLoginId}">
                            
                            <!-- 사용자에게 아이디 표시 (참고용) -->
                            <div class="info-group">
                                <p>아이디: ${memberLoginId}</p>
                            </div>

                            <div class="input-group">
                                <label for="newPassword">새 비밀번호</label>
                                <input type="password" id="newPassword" name="newPassword" 
                                       required pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                                       title="비밀번호는 8자 이상이며, 영문과 숫자를 포함해야 합니다.">
                            </div>
                            <div class="input-group">
                                <label for="confirmPassword">비밀번호 확인</label>
                                <input type="password" id="confirmPassword" name="confirmPassword" required>
                            </div>
                            <div class="button-group">
                                <button type="submit" class="submit-btn">비밀번호 변경</button>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="error-message">${error}</p>
                        <button type="button" onclick="showSearchForm()" class="back-btn">다시 찾기</button>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty success}">
        <script>
            document.getElementById('searchForm').style.display = 'none';
            document.getElementById('resultArea').style.display = 'block';
        </script>
    </c:if>
    <script src="${pageContext.request.contextPath}/static/js/findId.js"></script>
</body>
</html>