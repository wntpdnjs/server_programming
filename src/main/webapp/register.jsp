<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>팡팡 커뮤니티 - 회원가입</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .register-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <h2 class="text-center mb-4">회원가입</h2>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/user/register" method="post" id="registerForm">
                <div class="form-group">
                    <label for="username">아이디</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="username" name="username" required>
                        <div class="input-group-append">
                            <button type="button" class="btn btn-outline-secondary" onclick="checkUsername()">중복확인</button>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">비밀번호 확인</label>
                    <input type="password" class="form-control" id="confirmPassword" required>
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">가입하기</button>
            </form>
            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/login.jsp">로그인으로 돌아가기</a>
            </div>
        </div>
    </div>

    <script>
        function checkUsername() {
            const username = document.getElementById('username').value;
            if (!username) {
                alert('아이디를 입력해주세요.');
                return;
            }
            
            fetch('${pageContext.request.contextPath}/user/check-username?username=' + username)
                .then(response => response.json())
                .then(data => {
                    if (data.available) {
                        alert('사용 가능한 아이디입니다.');
                    } else {
                        alert('이미 사용 중인 아이디입니다.');
                    }
                });
        }

        document.getElementById('registerForm').onsubmit = function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                alert('비밀번호가 일치하지 않습니다.');
                e.preventDefault();
                return false;
            }
            return true;
        };
    </script>
</body>
</html> 