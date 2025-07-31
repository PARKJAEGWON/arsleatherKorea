<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 등록</title>
    <link rel="stylesheet" href="/static/css/admin.css">
    <link rel="stylesheet" href="/static/css/adminNotice.css">
    <!-- jQuery & Summernote CDN -->
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.20/lang/summernote-ko-KR.min.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <h2>공지사항 등록</h2>
        <form action="${pageContext.request.contextPath}/admin/kmw/notice/create" method="post" enctype="multipart/form-data" class="notice-create-form">
            <div class="notice-top-checkbox">
                <span>최상위 여부</span>
                <input type="checkbox" id="noticeTop" name="noticeTop" value="1">
            </div>
            <div class="form-group title-group">
                <label for="noticeTitle">제목</label>
                <input type="text" id="noticeTitle" name="noticeTitle" required>
                <input type="hidden" id="noticeStatus" name="noticeStatus" value="0">
            </div>
            <div class="form-group">
                <label for="noticeContent">내용</label>
                <textarea id="noticeContent" name="noticeContent" rows="10" required></textarea>
            </div>
            <div class="button-group">
                <button type="submit" class="btn">등록</button>
                <a href="${pageContext.request.contextPath}/admin/kmw/notice" class="btn">취소</a>
            </div>
        </form>
    </div>
</main>
<script src="/static/js/adminNotice.js"></script>
<script src="/static/js/admin.js"></script>
</body>
</html>