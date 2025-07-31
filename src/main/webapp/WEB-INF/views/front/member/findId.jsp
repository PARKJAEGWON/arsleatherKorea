<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>아이디 찾기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/terms.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/find.css">
</head>
<body class="terms-popup">
    <h2>아이디 찾기</h2>
    <div class="content">
        <!-- 폼 영역 -->
        <div id="searchForm">
            <form action="${pageContext.request.contextPath}/member/findId" method="post">
                <div class="input-group">
                    <label for="memberName">이름</label>
                    <input type="text" id="memberName" name="memberName" required>
                </div>
                
                <div class="input-group">
                    <label for="memberBirthDate">생년월일</label>
                    <input type="date" id="memberBirthDate" name="memberBirthDate" required>
                </div>
                
                <div class="input-group">
                    <label for="memberEmail">이메일</label>
                    <input type="email" id="memberEmail" name="memberEmail" required>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="submit-btn">아이디 찾기</button>
                </div>
            </form>
        </div>

        <!-- 결과 표시 영역 -->
        <div id="resultArea" class="result-area" style="display: none">
            <c:if test="${not empty success}">
                <c:choose>
                    <c:when test="${success}">
                        <p>아이디: <span class="masked-id">${maskedId}</span></p>
                        <button type="button" onclick="window.close()" class="back-btn">창 닫기</button>
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