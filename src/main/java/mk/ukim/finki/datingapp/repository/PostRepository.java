package mk.ukim.finki.datingapp.repository;

import mk.ukim.finki.datingapp.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
