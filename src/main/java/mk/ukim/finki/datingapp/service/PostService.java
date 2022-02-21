package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    List<Post> findAll();

    Post findPostById(Long id);

    void addPost(String content, HttpServletRequest request);

    void likePost(Long id, boolean like, HttpServletRequest request);
}
