package com.pangpang.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pangpang.model.User;
import com.pangpang.service.UserService;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println("UserServlet path: " + path);
        
        if ("/register".equals(path)) {
            try {
                // 회원가입 처리
                User user = new User();
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                
                System.out.println("Registering user: " + username);
                
                user.setUsername(username);
                user.setPassword(password);
                user.setName(name);
                
                if (userService.register(user)) {
                    System.out.println("Registration successful for: " + username);
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                } else {
                    System.out.println("Registration failed for: " + username);
                    request.setAttribute("error", "회원가입에 실패했습니다.");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
            } catch (Exception e) {
                System.out.println("Error during registration: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } else if ("/login".equals(path)) {
            // 로그인 처리
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            System.out.println("Login attempt for: " + username);
            
            User user = userService.login(username, password);
            if (user != null) {
                System.out.println("Login successful for: " + username);
                request.getSession().setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                System.out.println("Login failed for: " + username);
                request.setAttribute("error", "로그인에 실패했습니다.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println("UserServlet GET path: " + path);
        
        if ("/check-username".equals(path)) {
            String username = request.getParameter("username");
            System.out.println("Checking username availability: " + username);
            
            boolean isAvailable = userService.isUsernameAvailable(username);
            response.setContentType("application/json");
            response.getWriter().write("{\"available\": " + isAvailable + "}");
        } else if ("/logout".equals(path)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
} 