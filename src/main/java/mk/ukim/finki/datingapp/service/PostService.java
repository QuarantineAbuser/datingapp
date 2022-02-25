package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> findAll();

    Optional<Post> findById(Long id);

    void addPost(String content);

    void likePost(Long id, boolean like);
}
