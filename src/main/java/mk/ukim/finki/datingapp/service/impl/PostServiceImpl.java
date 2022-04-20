package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.exceptions.InvalidPostException;
import mk.ukim.finki.datingapp.repository.PostRepository;
import mk.ukim.finki.datingapp.service.PostService;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public void addPost(String content) {
        if (content.isEmpty())
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

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> filterPosts(String keyword) {
        return postRepository.findAllByContentContains(keyword);
    }

    @Override
    public List<Post> sortPosts(List<Post> posts, String sort) {
        Comparator<Post> comparator;
        if (sort.contains("Date")) {
            comparator = Comparator.comparing(Post::getDateCreated);
            if (sort.equals("ascDate"))
                comparator = comparator.reversed();
        } else {
            comparator = Comparator.comparing(p -> p.getLikes().size());
            if (sort.equals("ascLikes"))
                comparator = comparator.reversed();
        }
        return posts.stream().sorted(comparator).collect(Collectors.toList());
    }
}
