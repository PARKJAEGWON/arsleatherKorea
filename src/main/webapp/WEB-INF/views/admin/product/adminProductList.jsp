<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>상품관리</title>
    <link rel="stylesheet" href="/static/css/admin.css">
</head>
<body>
<%@ include file="/WEB-INF/views/admin/common/layout.jsp" %>
<main>
    <div class="main-content">
        <div style="display: flex; align-items: center; justify-content: space-between;">
            <h2>상품 목록</h2>
            <div class="button-group">
                <a href="${pageContext.request.contextPath}/admin/kmw/product/create" class="btn">상품 등록</a>
            </div>
        </div>
        
        <!-- <div class="product-layout">
            <div class="brand-section">
                <h3>브랜드</h3>
                <ul class="brand-list">
                    <li class="brand-item">
                        <a href="#" class="brand-link">전체</a>
                    </li>
                    <li class="brand-item">
                        <a href="#" class="brand-link">ARS LEATHER</a>
                    </li>
                    <li class="brand-item">
                        <a href="#" class="brand-link">UNBLOWN</a>
                    </li>
                </ul>
            </div> -->

            <!-- 상품 목록 테이블 -->
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>상품 일련번호</th>
                        <th>상품명</th>
                        <th>카테고리</th>
                        <th>소재</th>
                        <th>색상</th>
                        <th>성별</th>
                        <th>시즌 상품</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productPage.content}" varStatus="status">
                        <tr>
                            <!-- <td>${productPage.totalElements - (productPage.number * productPage.size) - status.index}</td> -->
                            <td>${product.id}</td>
                            <td>${product.productCode}</td>
                            <td>${product.productName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.productCategory == 0}">서류가방</c:when>
                                    <c:when test="${product.productCategory == 1}">백팩</c:when>
                                    <c:when test="${product.productCategory == 2}">핸드백</c:when>
                                    <c:otherwise>기타</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${product.productMaterial}</td>
                            <td>${product.productColor}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.productGender == 0}">공용</c:when>
                                    <c:when test="${product.productGender == 1}">남성</c:when>
                                    <c:when test="${product.productGender == 2}">여성</c:when>
                                    <c:otherwise>기타</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${product.productIsSeasonal ? '예' : '아니오'}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/kmw/product/detail?id=${product.id}">상세</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty productPage.content}">
                        <tr>
                            <td colspan="7">등록된 상품이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
            <!-- 페이징 UI -->
            <div class="pagination">
                <c:if test="${totalPages > 0}">
                    <!-- 이전 버튼 -->
                    <c:choose>
                        <c:when test="${currentPage > 0}">
                            <a href="?page=${currentPage - 1}&size=${pageSize}" class="page-link">이전</a>
                        </c:when>
                        <c:otherwise>
                            <span class="page-link disabled">이전</span>
                        </c:otherwise>
                    </c:choose>

                    <!-- 페이지 번호 -->
                    <c:forEach begin="0" end="${totalPages - 1}" var="pageNum">
                        <c:if test="${pageNum == currentPage}">
                            <span class="page-link active">${pageNum + 1}</span>
                        </c:if>
                        <c:if test="${pageNum != currentPage}">
                            <a href="?page=${pageNum}&size=${pageSize}" class="page-link">${pageNum + 1}</a>
                        </c:if>
                    </c:forEach>

                    <!-- 다음 버튼 -->
                    <c:choose>
                        <c:when test="${currentPage < totalPages - 1}">
                            <a href="?page=${currentPage + 1}&size=${pageSize}" class="page-link">다음</a>
                        </c:when>
                        <c:otherwise>
                            <span class="page-link disabled">다음</span>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>
    </div>
</main>
<script src="/static/js/admin.js"></script>
</body>
</html> 