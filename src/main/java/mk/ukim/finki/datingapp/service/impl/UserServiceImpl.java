package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.models.exceptions.*;
import mk.ukim.finki.datingapp.repository.UserRepository;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final HttpServletRequest request;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    @Override
    public void register(String username, String password, String repeatPassword,
                         String name, String surname, Role role, int age,
                         String bio, String city, Sex sex, String phoneNumber) {
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if (userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        if(!phoneNumber.chars().allMatch(Character::isDigit))
            throw new InvalidArgumentsException("phone number");
        if(bio == null || bio.isEmpty())
            bio = "No biography.";
        if(age > 105 || age < 0)
            throw new InvalidArgumentsException("age");
        if(age > 0 && age < 18)
            throw new InvalidAgeException();
        if(password.length() < 5)
            throw new WeakPasswordException();
        if(!name.chars().allMatch(Character::isAlphabetic))
            throw new InvalidArgumentsException("name");
        if(!surname.chars().allMatch(Character::isAlphabetic))
            throw new InvalidArgumentsException("surname");

        User user = new User(username, passwordEncoder.encode(password), name, surname,
                role, age, bio, city, sex, phoneNumber);
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers(String username) {
        List<User> users = userRepository.findAll();
        List<User> admins = this.findAllAdmins();

        users.remove(this.getActiveUser());
        users.removeAll(admins);
        return users;
    }

    @Override
    public List<User> findAllAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(Role.ROLE_ADMIN))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersFor(String username) {
        User activeUser = this.getActiveUser();
        List<User> users = this.findAllUsers(username);
        users.removeAll(activeUser.getInterestedIn());
        users.removeAll(this.findLikedByFor(username));

        return users;
    }

    @Override
    public List<User> findInterestedFor(String username) {
        return this.getActiveUser().getInterestedIn();
    }

    @Override
    public List<User> findLikedByFor(String username) {
        List<User> users = findAllUsers(username);

        return users.stream()
                .filter(u -> u.getLikes().contains(this.getActiveUser()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getActiveUser() {
        String activeUsername = request.getRemoteUser();
        return userRepository.findByUsername(activeUsername)
                .orElseThrow(() -> new UserNotFoundException(activeUsername));
    }

    public void interest(String username, String otherUsername, boolean interested) {
        User user = this.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        User otherUser = this.findByUsername(otherUsername)
                .orElseThrow(() -> new UserNotFoundException(otherUsername));
        if(interested)
            user.addInterestedIn(otherUser);
        else
            user.getInterestedIn().remove(otherUser);

        userRepository.save(otherUser);
    }

    @Override
    public void like(String username, String otherUsername, boolean like) {
        User user = this.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        User otherUser = this.findByUsername(otherUsername)
                .orElseThrow(() -> new UserNotFoundException(otherUsername));

        if(like)
            user.addLikes(otherUser);
        else
            user.getLikes().remove(otherUser);

        userRepository.save(otherUser);
    }

    @Override
    public void match(User user1, User user2, boolean match) {
        if (!user1.getMatched().contains(user2) || !user2.getMatched().contains(user1)) {
            if(match) {
                user1.addMatched(user2);
                user2.addMatched(user1);
            }
            else {
                user1.getMatched().remove(user2);
                user2.getMatched().remove(user1);
            }
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    @Override
    public void updateMatches(String username) {
        User user = this.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        this.findLikedByFor(username).forEach(otherUser -> {
            if (user.getLikes().contains(otherUser) && otherUser.getLikes().contains(user))
                this.match(user, otherUser, true);
            if (!user.getLikes().contains(otherUser) || !otherUser.getLikes().contains(user))
                this.match(user, otherUser, false);
        });
    }

    @Override
    public List<User> filterUsers(List<User> users, String keyword, String age, String city, String sex) {
        List<User> filteredUsers = users;
        if (keyword != null && !keyword.isEmpty())
            filteredUsers = userRepository.findAllByNameContainsOrSurnameContains(keyword, keyword);
        if (age != null && !age.isEmpty())
            filteredUsers = filteredUsers.stream().filter(u -> u.getAge() == Integer.parseInt(age)).collect(Collectors.toList());
        if (city != null && !city.isEmpty())
            filteredUsers = filteredUsers.stream().filter(u -> u.getCity().equals(city)).collect(Collectors.toList());
        if (sex != null && !sex.isEmpty() && !sex.equals("ALL"))
            filteredUsers = filteredUsers.stream().filter(u -> u.getSex().name().equals(sex)).collect(Collectors.toList());

        filteredUsers.remove(this.getActiveUser());
        filteredUsers.removeAll(this.getActiveUser().getInterestedIn());
        filteredUsers.removeAll(this.findLikedByFor(this.getActiveUser().getUsername()));
        return filteredUsers;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
