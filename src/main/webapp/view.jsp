<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>팡팡 커뮤니티 - 게시글 보기</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .post-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .post-image {
            max-width: 100%;
            margin: 20px 0;
        }
        .post-info {
            color: #666;
            font-size: 0.9em;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">팡팡 커뮤니티</a>
            <div class="navbar-nav ml-auto">
                <c:if test="${not empty sessionScope.user}">
                    <span class="nav-item nav-link">${sessionScope.user.name}님 환영합니다</span>
                    <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
                </c:if>
            </div>
        </div>
    </nav>

    <div class="container post-container">
        <h2>${post.title}</h2>
        <div class="post-info">
            작성자: ${post.authorName} | 작성일: <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
        </div>
        <div class="post-content">
            <p>${post.content}</p>
            <c:if test="${not empty post.imagePath}">
                <img src="${pageContext.request.contextPath}${post.imagePath}" class="post-image" alt="게시글 이미지">
            </c:if>
        </div>
        
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/posts" class="btn btn-secondary">목록으로</a>
            <c:if test="${sessionScope.user.userId eq post.userId}">
                <a href="${pageContext.request.contextPath}/post/edit/${post.postId}" class="btn btn-primary">수정</a>
                <button onclick="deletePost(${post.postId})" class="btn btn-danger">삭제</button>
            </c:if>
        </div>
    </div>

    <script>
        function deletePost(postId) {
            if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
                fetch('${pageContext.request.contextPath}/post/delete/' + postId, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.href = '${pageContext.request.contextPath}/posts';
                    } else {
                        alert('게시글 삭제에 실패했습니다.');
                    }
                }).catch(error => {
                    console.error('Error:', error);
                    alert('게시글 삭제 중 오류가 발생했습니다.');
                });
            }
        }
    </script>
</body>
</html> 