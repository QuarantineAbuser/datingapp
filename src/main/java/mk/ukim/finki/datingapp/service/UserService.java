package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    void register(String username, String password, String repeatPassword,
                  String name, String surname, Role role,
                  int age, String bio, String city, Sex sex, String phoneNumber);

    List<User> findAllUsers(String activeUsername);

    List<User> findAllAdmins();

    List<User> findUsersFor(String username);

    List<User> findInterestedFor(String username);

    List<User> findLikedByFor(String username);

    Optional<User> findByUsername(String username);

    User getActiveUser();

    void interest(String username, String otherUsername, boolean interest);

    void like(String username, String otherUsername, boolean like);

    void match(User user1, User user2, boolean match);

    void updateMatches(String username);

    List<User> filterUsers(List<User> users, String keyword, String age, String city, String sex);
}
