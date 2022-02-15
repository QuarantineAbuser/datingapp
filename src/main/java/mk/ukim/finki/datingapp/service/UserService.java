package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword, String name, String surname, Role role);

}
