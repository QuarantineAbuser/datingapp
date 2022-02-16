package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> listAll();

    Optional<User> findByUsername(String username);

    User register(String username, String password, String repeatPassword,
                  String name, String surname, Role role,
                  int age, String bio, String city, Sex sex);

    void interest(User user, User interested);

    void uninterested(User user, User uninterested);

    void like(User user, User liked);

    void match(User user1, User user2);
}
