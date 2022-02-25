package mk.ukim.finki.datingapp.config;

import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        if (userService.findByUsername("admin").isEmpty())
            this.userService.register("admin", "admin", "admin"
                    , "Stefan", "Mitrevski", Role.ROLE_ADMIN, 21, "admin"
                    , "Prilep", Sex.MALE, "070956784");
    }
}
