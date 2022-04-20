package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll();

    void addPost(String content);

    void likePost(Long id, boolean like);

    void deletePost(Long id);

    List<Post> filterPosts(String keyword);

    List<Post> sortPosts(List<Post> posts, String sort);
}
