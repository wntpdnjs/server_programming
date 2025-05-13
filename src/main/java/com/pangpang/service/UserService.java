package com.pangpang.service;

import com.pangpang.dao.UserDAO;
import com.pangpang.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean register(User user) {
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

    // ✅ 전체 사용자 목록 조회
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // ✅ 사용자 삭제
    public boolean deleteUser(String id) {
        return userDAO.deleteById(id);
    }
}
