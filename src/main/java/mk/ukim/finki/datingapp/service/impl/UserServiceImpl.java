package mk.ukim.finki.datingapp.service.impl;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.exceptions.InvalidArgumentsException;
import mk.ukim.finki.datingapp.models.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.datingapp.models.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.datingapp.repository.UserRepository;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role role) {
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

        User user = new User(username, passwordEncoder.encode(password), name, username, role);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
