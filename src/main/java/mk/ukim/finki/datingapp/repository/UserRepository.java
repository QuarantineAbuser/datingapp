package mk.ukim.finki.datingapp.repository;

import mk.ukim.finki.datingapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    User findByUsernameLike(String username);

    List<User> findAllByNameContainsOrSurnameContains(String keyword1, String keyword2);
}
