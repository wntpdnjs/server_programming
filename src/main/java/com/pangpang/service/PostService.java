package com.pangpang.service;

import java.util.List;
import com.pangpang.dao.PostDAO;
import com.pangpang.model.Post;

public class PostService {
    private PostDAO postDAO = new PostDAO();
    
    public List<Post> getAllPosts() {
        return postDAO.getAllPosts();
    }
    
    public Post getPost(long postId) {
        return postDAO.getPost(postId);
    }
    
    public boolean createPost(Post post) {
        return postDAO.insertPost(post);
    }
    
    public boolean updatePost(Post post) {
        return postDAO.updatePost(post);
    }
    
    public boolean deletePost(long postId) {
        return postDAO.deletePost(postId);
    }
    
    // 추가 메서드들
} 