package com.pangpang.service;

import com.pangpang.dao.UserDAO;
import com.pangpang.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    
    public boolean register(User user) {
        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        
        return userDAO.insertUser(user);
    }
    
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    public boolean isUsernameAvailable(String username) {
        return userDAO.findByUsername(username) == null;
    }
    
    // 추가 메서드들
} 