package mk.ukim.finki.datingapp.config;

import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.repository.UserRepository;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final UserService userService;

    private final UserRepository userRepository;

    public DataInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        if(userRepository.findByUsernameLike("admin") == null)
        this.userService.register("admin", "admin", "admin"
        ,"Stefan", "Mitrevski", Role.ROLE_ADMIN, 21, "admin"
        ,"Priep", Sex.MALE, "070956784");
    }
}
