<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>팡팡 커뮤니티 - 게시글 수정</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .edit-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        #currentImage {
            max-width: 100%;
            max-height: 300px;
            margin: 10px 0;
        }
        #imagePreview {
            max-width: 100%;
            max-height: 300px;
            margin: 10px 0;
            display: none;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">팡팡 커뮤니티</a>
            <div class="navbar-nav ml-auto">
                <span class="nav-item nav-link">${sessionScope.user.name}님 환영합니다</span>
                <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
            </div>
        </div>
    </nav>

    <div class="container edit-container">
        <h2 class="text-center mb-4">게시글 수정</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/post/update/${post.postId}" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" class="form-control" id="title" name="title" value="${post.title}" required>
            </div>
            <div class="form-group">
                <label for="content">내용</label>
                <textarea class="form-control" id="content" name="content" rows="10" required>${post.content}</textarea>
            </div>
            <div class="form-group">
                <label>현재 이미지</label>
                <c:if test="${not empty post.imagePath}">
                    <img id="currentImage" src="${pageContext.request.contextPath}${post.imagePath}" alt="현재 이미지">
                </c:if>
                <c:if test="${empty post.imagePath}">
                    <p>첨부된 이미지가 없습니다.</p>
                </c:if>
            </div>
            <div class="form-group">
                <label for="image">새 이미지 첨부</label>
                <input type="file" class="form-control-file" id="image" name="image" accept="image/*" onchange="previewImage(this)">
                <img id="imagePreview" class="img-fluid">
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">수정하기</button>
                <a href="${pageContext.request.contextPath}/post/view/${post.postId}" class="btn btn-secondary">취소</a>
            </div>
        </form>
    </div>

    <script>
        function previewImage(input) {
            const preview = document.getElementById('imagePreview');
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>
</html> 