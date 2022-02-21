package mk.ukim.finki.datingapp.config;

import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.repository.UserRepository;
import mk.ukim.finki.datingapp.service.PostService;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final UserService userService;

    private final UserRepository userRepository;

    private final PostService postService;

    public static User ACTIVE_USER;

    public DataInitializer(UserService userService, UserRepository userRepository, PostService postService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.postService = postService;
    }

    @PostConstruct
    public void init() {
        if(userRepository.findByUsernameLike("admin") == null)
        this.userService.register("admin", "admin", "admin"
        ,"Stefan", "Mitrevski", Role.ROLE_ADMIN, 21, "admin"
        ,"Priep", Sex.MALE, "070956784");

        //postService.addPost(new Post(userService.findByUsername("user2"), "content"));
    }
}
