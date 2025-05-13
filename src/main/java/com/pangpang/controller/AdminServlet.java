package com.pangpang.controller;

import com.pangpang.model.Post;
import com.pangpang.model.User;
import com.pangpang.service.PostService;
import com.pangpang.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();
    private PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 여부 및 관리자 권한 확인
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null || !"admin".equalsIgnoreCase(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        

        List<User> users = userService.getAllUsers();
        request.setAttribute("userList", users);

        // ✅ userId → name 매핑용 Map 생성
        Map<Long, String> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getUserId(), user.getName());
        }
        request.setAttribute("userMap", userMap);

        // 게시글 목록 조건에 따라 조회
        String action = request.getParameter("action");
        PostService postService = new PostService();

        List<Post> posts;
        if ("filterPosts".equals(action)) {
            String name = request.getParameter("username");
            posts = postService.getPostsByAuthorName(name);
        } else {
            posts = postService.getAllPosts();
        }
        request.setAttribute("postList", posts);

        // 최종 포워딩
        request.getRequestDispatcher("/admin.jsp").forward(request, response);

    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 및 관리자 권한 확인
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null || !"admin".equalsIgnoreCase(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String id = request.getParameter("id");
            userService.deleteUser(id); // 삭제 실행
        }
        
        if ("deletePost".equals(action)) {
            long postId = Long.parseLong(request.getParameter("postId"));
            postService.deletePost(postId); 
        }

        // 삭제 후 다시 목록 조회 화면으로 이동
        response.sendRedirect(request.getContextPath() + "/admin");
        
        
    }
    
    

}
