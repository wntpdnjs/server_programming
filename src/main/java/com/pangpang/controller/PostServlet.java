package com.pangpang.controller;

import java.io.IOException;
import java.io.File;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.pangpang.model.Post;
import com.pangpang.model.User;
import com.pangpang.service.PostService;

@WebServlet(urlPatterns = {"/post/*", "/posts"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 10,  // 10MB
    maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class PostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostService postService = new PostService();
    
    @Override
    public void init() throws ServletException {
        super.init();
        String uploadPath = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        
        System.out.println("PostServlet doGet - PathInfo: " + pathInfo + ", ServletPath: " + servletPath);
        
        if ("/posts".equals(servletPath)) {
            request.setAttribute("posts", postService.getAllPosts());
            request.getRequestDispatcher("/posts.jsp").forward(request, response);
            return;
        }
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/posts");
            return;
        }
        
        try {
            if (pathInfo.startsWith("/view/")) {
                String postId = pathInfo.substring(6);
                System.out.println("Viewing post with ID: " + postId);
                Post post = postService.getPost(Long.parseLong(postId));
                if (post != null) {
                    System.out.println("Found post: " + post.getTitle());
                    request.setAttribute("post", post);
                    request.getRequestDispatcher("/view.jsp").forward(request, response);
                } else {
                    System.out.println("Post not found");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else if (pathInfo.startsWith("/edit/")) {
                String postId = pathInfo.substring(6);
                System.out.println("Editing post with ID: " + postId);
                Post post = postService.getPost(Long.parseLong(postId));
                if (post != null) {
                    User user = (User) request.getSession().getAttribute("user");
                    if (user != null && user.getUserId() == post.getUserId()) {
                        System.out.println("User authorized to edit post");
                        request.setAttribute("post", post);
                        request.getRequestDispatcher("/edit.jsp").forward(request, response);
                        return;
                    }
                    System.out.println("User not authorized to edit post");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    System.out.println("Post not found");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/posts");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid post ID format: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("PostServlet doPost - PathInfo: " + pathInfo);
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            if ("/create".equals(pathInfo)) {
                handleCreatePost(request, response, user);
            } else if (pathInfo.startsWith("/update/")) {
                String postId = pathInfo.substring(8);
                handleUpdatePost(request, response, user);
            } else if (pathInfo.startsWith("/delete/")) {
                String postId = pathInfo.substring(8);
                handleDeletePost(request, response, user);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid post ID format: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void handleCreatePost(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            Post post = new Post();
            post.setUserId(user.getUserId());
            post.setTitle(request.getParameter("title"));
            post.setContent(request.getParameter("content"));
            
            handleImageUpload(request, post);
            
            if (postService.createPost(post)) {
                response.sendRedirect(request.getContextPath() + "/posts");
            } else {
                request.setAttribute("error", "게시글 작성에 실패했습니다.");
                request.getRequestDispatcher("/write.jsp").forward(request, response);
            }
        } catch (Exception e) {
            handleError(request, response, e, "/write.jsp");
        }
    }
    
    private void handleUpdatePost(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String postId = request.getPathInfo().substring(8);
            System.out.println("Updating post with ID: " + postId);
            Post existingPost = postService.getPost(Long.parseLong(postId));
            
            if (existingPost != null && existingPost.getUserId() == user.getUserId()) {
                Post updatedPost = new Post();
                updatedPost.setPostId(Long.parseLong(postId));
                updatedPost.setUserId(user.getUserId());
                updatedPost.setTitle(request.getParameter("title"));
                updatedPost.setContent(request.getParameter("content"));
                updatedPost.setImagePath(existingPost.getImagePath());
                
                // 새 이미지가 업로드된 경우에만 이미지 처리
                Part filePart = request.getPart("image");
                if (filePart != null && filePart.getSize() > 0) {
                    // 기존 이미지 파일 삭제
                    if (existingPost.getImagePath() != null) {
                        String oldImagePath = getServletContext().getRealPath(existingPost.getImagePath());
                        new File(oldImagePath).delete();
                    }
                    
                    // 새 이미지 업로드
                    handleImageUpload(request, updatedPost);
                }
                
                System.out.println("Attempting to update post in database");
                if (postService.updatePost(updatedPost)) {
                    System.out.println("Post updated successfully");
                    response.sendRedirect(request.getContextPath() + "/post/view/" + postId);
                } else {
                    System.out.println("Failed to update post");
                    request.setAttribute("error", "게시글 수정에 실패했습니다.");
                    request.setAttribute("post", updatedPost);
                    request.getRequestDispatcher("/edit.jsp").forward(request, response);
                }
            } else {
                System.out.println("User not authorized to update post");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            System.out.println("Error updating post: " + e.getMessage());
            e.printStackTrace();
            handleError(request, response, e, "/edit.jsp");
        }
    }
    
    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String postId = request.getPathInfo().substring(8);
            System.out.println("Deleting post with ID: " + postId);
            Post post = postService.getPost(Long.parseLong(postId));
            
            if (post != null && post.getUserId() == user.getUserId()) {
                System.out.println("Attempting to delete post from database");
                if (postService.deletePost(Long.parseLong(postId))) {
                    // 이미지 파일도 삭제
                    if (post.getImagePath() != null) {
                        String imagePath = getServletContext().getRealPath(post.getImagePath());
                        boolean deleted = new File(imagePath).delete();
                        System.out.println("Image deletion " + (deleted ? "successful" : "failed") + ": " + imagePath);
                    }
                    System.out.println("Post deleted successfully");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    System.out.println("Failed to delete post");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                System.out.println("User not authorized to delete post");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            System.out.println("Error deleting post: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void handleImageUpload(HttpServletRequest request, Post post) throws IOException, ServletException {
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            String uploadPath = getServletContext().getRealPath("/uploads");
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            String fullPath = uploadPath + File.separator + fileName;
            filePart.write(fullPath);
            post.setImagePath("/uploads/" + fileName);
        }
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, 
            Exception e, String jspPath) throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("error", "오류가 발생했습니다: " + e.getMessage());
        request.getRequestDispatcher(jspPath).forward(request, response);
    }
} 