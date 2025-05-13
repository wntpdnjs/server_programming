package com.pangpang.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.pangpang.model.User;
import com.pangpang.util.DatabaseUtil;

public class UserDAO {
    
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (user_id, username, password, name, role) VALUES (user_seq.NEXTVAL, ?, ?, ?, 'USER')";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setName(rs.getString("name"));
                    user.setRole(rs.getString("role"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean deleteById(String id) {
        String deletePostsSql = "DELETE FROM posts WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (
                PreparedStatement pstmtPosts = conn.prepareStatement(deletePostsSql);
                PreparedStatement pstmtUser = conn.prepareStatement(deleteUserSql)
            ) {
                long userId = Long.parseLong(id);

                // 게시글 먼저 삭제
                pstmtPosts.setLong(1, userId);
                pstmtPosts.executeUpdate();

                // 사용자 삭제
                pstmtUser.setLong(1, userId);
                int result = pstmtUser.executeUpdate();

                conn.commit();
                return result > 0;

            } catch (SQLException e) {
                conn.rollback(); // 오류 시 롤백
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
} 