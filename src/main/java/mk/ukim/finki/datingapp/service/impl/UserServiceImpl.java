package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.models.exceptions.InvalidArgumentsException;
import mk.ukim.finki.datingapp.models.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.datingapp.models.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.datingapp.repository.UserRepository;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User register(String username, String password, String repeatPassword,
                         String name, String surname, Role role,
                         int age, String bio, String city, Sex sex) {
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

        User user = new User(username, passwordEncoder.encode(password), name, username, role, age, bio, city, sex);
        return userRepository.save(user);
    }

    @Override
    public void interest(User user, User interested) {
        user.addInterestedIn(interested);
        userRepository.save(user);
    }

    @Override
    public void uninterested(User user, User uninterested) {
        user.getInterestedIn().remove(uninterested);
        userRepository.save(user);
    }

    @Override
    public void like(User user, User liked) {
        user.addLikes(liked);
    }

    @Override
    public void match(User user1, User user2) {
        user1.addMatched(user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
