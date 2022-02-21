package mk.ukim.finki.datingapp.repository;

import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByUser(User user);
}
