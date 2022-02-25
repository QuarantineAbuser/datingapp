package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.exceptions.InvalidPostException;
import mk.ukim.finki.datingapp.repository.PostRepository;
import mk.ukim.finki.datingapp.service.PostService;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public void addPost(String content) {
        if(content.isEmpty())
            throw new InvalidPostException();
        postRepository.save(new Post(userService.getActiveUser(), content));
    }

    @Override
    public void likePost(Long id, boolean like) {
        Post post = postRepository.getById(id);

        if (like)
            post.addLike(userService.getActiveUser());
        else
            post.getLikes().remove(userService.getActiveUser());

        postRepository.save(post);
    }
}
