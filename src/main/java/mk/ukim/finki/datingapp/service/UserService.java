package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword,
                  String name, String surname, Role role,
                  int age, String bio, String city, Sex sex, String phoneNumber);

    List<User> findAll(String activeUsername);

    List<User> findUsersFor(String username);

    List<User> findInterestedFor(String username);

    List<User> findLikedByFor(String username);

    User findByUsername(String username);

    User getActiveUser(HttpServletRequest request);

    void interested(String activeUsername,String interestedUsername);

    void uninterested(String activeUsername, String uninterestedUsername);

    void like(String activeUsername, String likedUsername);

    void unlike(String activeUsername, String unlikedUsername);

    void match(User user1, User user2);

    void updateMatches(String username);

    void unmatch(User user1, User user2);

    List<User> filterUsers(List<User> users, String keyword, String age, String city, String sex, String username);
}
