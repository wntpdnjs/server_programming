<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 페이지</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">팡팡 커뮤니티 관리자 페이지</a>
            <div class="navbar-nav ml-auto">
                <c:if test="${not empty sessionScope.user}">
                    <span class="nav-item nav-link">${sessionScope.user.name}님 환영합니다</span>
                    <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
                </c:if>
            </div>
        </div>
    </nav>

    <h3 class="mt-4">회원 목록</h3>
    <c:if test="${not empty userList}">
        <table class="table table-bordered mt-3">
            <thead class="thead-light">
                <tr>
                    <th>ID</th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>권한</th>
                    <th>가입일</th>
                    <th>삭제</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                <c:if test="${user.userId != sessionScope.user.userId}">
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.username}</td>
                        <td>${user.name}</td>
                        <td>${user.role}</td>
                        <td>${user.createdAt}</td>
                        
                        <td>
				            <form method="post" action="${pageContext.request.contextPath}/admin"
				                  onsubmit="return confirm('정말 삭제하시겠습니까?');">
				                <input type="hidden" name="action" value="delete">
				                <input type="hidden" name="id" value="${user.userId}">
				                <button type="submit" class="btn btn-danger btn-sm">삭제</button>
				            </form>
				        </td>
                    </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    
    <c:if test="${empty userList}">
        <div class="alert alert-info mt-3">등록된 사용자가 없습니다.</div>
    </c:if>
    <hr class="my-5">

<h3 class="mt-4">게시글 목록</h3>

<!-- 🔍 사용자 이름 검색 폼 -->
<form class="form-inline mb-3" method="get" action="${pageContext.request.contextPath}/admin">
    <input type="hidden" name="action" value="filterPosts">
    <input type="text" name="username" class="form-control mr-2" placeholder="사용자 아이디 검색">
    <button type="submit" class="btn btn-primary">검색</button>
    <a href="${pageContext.request.contextPath}/admin?action=viewPosts" class="btn btn-secondary ml-2">전체 보기</a>
</form>

<!-- 게시글 테이블 -->
<c:if test="${not empty postList}">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>글 ID</th>
                <th>작성자</th>
                <th>제목</th>
                <th>내용</th>
                <th>작성일</th>
                <th>삭제</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="post" items="${postList}">
                <tr>
                    <td>${post.postId}</td>
                    <td>${userMap[post.userId]}</td>
                    <td>${post.title}</td>
                    <td>${post.content}</td>
                    <td>${post.createdAt}</td>
                    <td>
				            <form method="post" action="${pageContext.request.contextPath}/admin"
				                  onsubmit="return confirm('정말 삭제하시겠습니까?');">
				                <input type="hidden" name="action" value="deletePost">
				                <input type="hidden" name="postId" value="${post.postId}">
				                <button type="submit" class="btn btn-danger btn-sm">삭제</button>
				            </form>
				        </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${empty postList}">
    <div class="alert alert-info">표시할 게시글이 없습니다.</div>
</c:if>
    
</div>
</body>
</html>
