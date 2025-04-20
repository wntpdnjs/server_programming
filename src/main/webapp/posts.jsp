<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>팡팡 커뮤니티 - 게시글 목록</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .post-container {
            margin: 20px auto;
            max-width: 800px;
        }
        .post-card {
            margin-bottom: 20px;
            transition: transform 0.2s;
        }
        .post-card:hover {
            transform: translateY(-5px);
        }
        .post-image {
            max-height: 200px;
            object-fit: cover;
        }
    </style>
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

    <div class="container post-container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>게시글 목록</h2>
            <c:if test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/write.jsp" class="btn btn-primary">글쓰기</a>
            </c:if>
        </div>

        <div class="row">
            <c:forEach items="${posts}" var="post">
                <div class="col-md-6">
                    <div class="card post-card">
                        <c:if test="${not empty post.imagePath}">
                            <img src="${pageContext.request.contextPath}${post.imagePath}" class="card-img-top post-image" alt="게시글 이미지">
                        </c:if>
                        <div class="card-body">
                            <h5 class="card-title">${post.title}</h5>
                            <p class="card-text">${post.content}</p>
                            <div class="d-flex justify-content-between align-items-center">
                                <small class="text-muted">
                                    작성자: ${post.authorName}<br>
                                    작성일: <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                </small>
                                <div>
                                    <a href="${pageContext.request.contextPath}/post/view/${post.postId}" class="btn btn-sm btn-outline-primary">상세보기</a>
                                    <c:if test="${sessionScope.user.userId == post.userId}">
                                        <a href="${pageContext.request.contextPath}/post/edit/${post.postId}" class="btn btn-sm btn-outline-secondary">수정</a>
                                        <button onclick="deletePost(${post.postId})" class="btn btn-sm btn-outline-danger">삭제</button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script>
        function deletePost(postId) {
            if (confirm('정말 삭제하시겠습니까?')) {
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