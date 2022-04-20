package mk.ukim.finki.datingapp.repository;

import mk.ukim.finki.datingapp.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByContentContains(String keyword);
}
