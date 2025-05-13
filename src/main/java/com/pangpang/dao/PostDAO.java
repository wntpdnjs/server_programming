package com.pangpang.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.pangpang.model.Post;
import com.pangpang.util.DatabaseUtil;

public class PostDAO {
    
    public boolean insertPost(Post post) {
        String sql = "INSERT INTO posts (post_id, user_id, title, content, image_path) VALUES (post_seq.NEXTVAL, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, post.getUserId());
            pstmt.setString(2, post.getTitle());
            pstmt.setString(3, post.getContent());
            pstmt.setString(4, post.getImagePath());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.name as author_name FROM posts p JOIN users u ON p.user_id = u.user_id ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getLong("post_id"));
                post.setUserId(rs.getLong("user_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setImagePath(rs.getString("image_path"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setUpdatedAt(rs.getTimestamp("updated_at"));
                post.setAuthorName(rs.getString("author_name"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    
    public Post getPost(long postId) {
        String sql = "SELECT p.*, u.name as author_name FROM posts p JOIN users u ON p.user_id = u.user_id WHERE p.post_id = ?";
        System.out.println("Executing SQL: " + sql + " with postId: " + postId);
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, postId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post();
                    post.setPostId(rs.getLong("post_id"));
                    post.setUserId(rs.getLong("user_id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setImagePath(rs.getString("image_path"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setUpdatedAt(rs.getTimestamp("updated_at"));
                    post.setAuthorName(rs.getString("author_name"));
                    System.out.println("Found post: " + post.getTitle());
                    return post;
                }
                System.out.println("No post found with ID: " + postId);
            }
        } catch (SQLException e) {
            System.out.println("Error getting post: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updatePost(Post post) {
        String sql = "UPDATE posts SET title = ?, content = ?, image_path = ?, updated_at = CURRENT_TIMESTAMP WHERE post_id = ? AND user_id = ?";
        System.out.println("Executing SQL: " + sql + " with postId: " + post.getPostId());
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getImagePath());
            pstmt.setLong(4, post.getPostId());
            pstmt.setLong(5, post.getUserId());
            
            int result = pstmt.executeUpdate();
            System.out.println("Update result: " + result + " rows affected");
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error updating post: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePost(long postId) {
        String sql = "DELETE FROM posts WHERE post_id = ?";
        System.out.println("Executing SQL: " + sql + " with postId: " + postId);
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, postId);
            
            int result = pstmt.executeUpdate();
            System.out.println("Delete result: " + result + " rows affected");
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting post: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<Post> getPostsByAuthorName(String name) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.* FROM posts p JOIN users u ON p.user_id = u.user_id WHERE u.name LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setPostId(rs.getLong("post_id"));
                    post.setUserId(rs.getLong("user_id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setImagePath(rs.getString("image_path"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setUpdatedAt(rs.getTimestamp("updated_at"));

                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    
    // 추가 메서드들 (게시글 수정, 삭제, 검색 등)
} 