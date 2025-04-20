<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>팡팡 커뮤니티 - 글쓰기</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .write-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        #imagePreview {
            max-width: 100%;
            max-height: 300px;
            margin-top: 10px;
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

    <div class="container write-container">
        <h2 class="text-center mb-4">글쓰기</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/post/create" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="form-group">
                <label for="content">내용</label>
                <textarea class="form-control" id="content" name="content" rows="10" required></textarea>
            </div>
            <div class="form-group">
                <label for="image">이미지 첨부</label>
                <input type="file" class="form-control-file" id="image" name="image" accept="image/*" onchange="previewImage(this)">
                <img id="imagePreview" class="img-fluid">
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">작성하기</button>
                <a href="${pageContext.request.contextPath}/posts" class="btn btn-secondary">취소</a>
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