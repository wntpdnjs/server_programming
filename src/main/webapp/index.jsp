<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>팡팡 커뮤니티</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">팡팡 커뮤니티</a>
            <div class="navbar-nav ml-auto">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a class="nav-item nav-link" href="${pageContext.request.contextPath}/login.jsp">로그인</a>
                        <a class="nav-item nav-link" href="${pageContext.request.contextPath}/register.jsp">회원가입</a>
                    </c:when>
                    <c:otherwise>
                        <span class="nav-item nav-link">${sessionScope.user.name}님 환영합니다</span>
                        <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="jumbotron">
            <h1 class="display-4">팡팡 커뮤니티에 오신 것을 환영합니다!</h1>
            <p class="lead">자유롭게 글을 작성하고 다른 사용자들과 소통해보세요.</p>
            <hr class="my-4">
            <p>글을 작성하거나 읽으려면 로그인이 필요합니다.</p>
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/login.jsp" role="button">로그인하기</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/posts" role="button">게시글 보기</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html> 