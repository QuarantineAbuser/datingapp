package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.config.DataInitializer;
import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.repository.PostRepository;
import mk.ukim.finki.datingapp.service.PostService;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void addPost(String content, HttpServletRequest request) {
        User user = userService.getActiveUser(request);
        postRepository.save(new Post(user, content));
    }

    @Override
    public void likePost(Long id, boolean like, HttpServletRequest request) {
        Post post = postRepository.getById(id);

        if(like)
            post.addLike(userService.getActiveUser(request));
        else
            post.getLikes().remove(userService.getActiveUser(request));

        postRepository.save(post);
    }
}
