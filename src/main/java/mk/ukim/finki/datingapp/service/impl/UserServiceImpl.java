package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.models.exceptions.InvalidArgumentsException;
import mk.ukim.finki.datingapp.models.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.datingapp.models.exceptions.UserNotFoundException;
import mk.ukim.finki.datingapp.models.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.datingapp.repository.UserRepository;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password, String repeatPassword,
                         String name, String surname, Role role, int age,
                         String bio, String city, Sex sex, String phoneNumber) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty() ||
                repeatPassword == null || repeatPassword.isEmpty() || name == null || name.isEmpty() ||
                surname == null || surname.isEmpty() || role == null) {
            throw new InvalidArgumentsException();
        }
        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        if (userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);

        User user = new User(username, passwordEncoder.encode(password), name, surname,
                role, age, bio, city, sex, phoneNumber);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(String activeUsername) {
        List<User> users = userRepository.findAll();
        List<User> admins = users.stream()
                .filter(u -> u.getRole().equals(Role.ROLE_ADMIN))
                .collect(Collectors.toList());

        users.remove(this.findByUsername(activeUsername));
        users.removeAll(admins);

        return users;
    }

    @Override
    public List<User> findInterestedFor(String username) {
        User activeUser = this.findByUsername(username);
        return activeUser.getInterestedIn();
    }

    @Override
    public List<User> findLikedByFor(String username) {
        User activeUser = this.findByUsername(username);
        List<User> users = findAll(username);

        return users.stream().filter(u -> u.getLikes().contains(activeUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersFor(String username) {
        User activeUser = this.findByUsername(username);
        List<User> users = this.findAll(username);
        users.removeAll(activeUser.getInterestedIn());
        //users.removeAll(activeUser.getLikes());
        users.removeAll(this.findLikedByFor(username));

        return users;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User getActiveUser(HttpServletRequest request) {
        String username = request.getRemoteUser();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void interested(String activeUsername, String interestedUsername) {
        User activeUser = this.findByUsername(activeUsername);
        User interestedUser = this.findByUsername(interestedUsername);

        activeUser.addInterestedIn(interestedUser);
        userRepository.save(activeUser);
    }

    @Override
    public void uninterested(String activeUsername, String uninterestedUsername) {
        User activeUser = this.findByUsername(activeUsername);
        User uninterestedUser = this.findByUsername(uninterestedUsername);

        activeUser.getInterestedIn().remove(uninterestedUser);
        userRepository.save(activeUser);
    }

    @Override
    public void like(String activeUsername, String likedUsername) {
        User activeUser = this.findByUsername(activeUsername);
        User likedUser = this.findByUsername(likedUsername);

        activeUser.addLikes(likedUser);
        userRepository.save(activeUser);
    }

    @Override
    public void unlike(String activeUsername, String unlikedUsername) {
        User activeUser = this.findByUsername(activeUsername);
        User unlikedUser = this.findByUsername(unlikedUsername);

        activeUser.getLikes().remove(unlikedUser);
        userRepository.save(activeUser);
    }

//    @Override
//    public void removeLikedBy(String activeUsername, String username) {
//        User activeUser = this.findByUsername(activeUsername);
//        User unlikedUser = this.findByUsername(username);
//
//        activeUser.getLikes().remove(unlikedUser);
//        userRepository.save(activeUser);
//    }

    @Override
    public void match(User user1, User user2) {
        if(!user1.getMatched().contains(user2)
            || !user2.getMatched().contains(user1)) {
            user1.addMatched(user2);
            user2.addMatched(user1);
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    @Override
    public void updateMatches(String username) {
        User user = this.findByUsername(username);
        List<User> likedBy = this.findLikedByFor(username);
        likedBy.forEach(u -> {
            if (user.getLikes().contains(u))
                this.match(user, u);
            if(!u.getLikes().contains(user) || !user.getLikes().contains(u))
                this.unmatch(user, u);
        });
    }

    @Override
    public void unmatch(User user1, User user2) {
        if(user1.getMatched().contains(user2)
                || user2.getMatched().contains(user1)) {
            user1.getMatched().remove(user2);
            user2.getMatched().remove(user1);
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
