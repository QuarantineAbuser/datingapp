package mk.ukim.finki.datingapp.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class Post {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    User user;

    String content;

    LocalDateTime dateCreated;

    @ManyToMany
    List<User> likes;

    public Post() {
        this.likes = new ArrayList<>();
        this.dateCreated = LocalDateTime.now();
    }

    public Post(User user, String content) {
        this.user = user;
        this.dateCreated = LocalDateTime.now();
        this.content = content;
        this.likes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void addLike(User user) {
        this.likes.add(user);
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @SuppressWarnings("unused")
    public String getFormattedDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM, uuu · KK:mm a", Locale.ENGLISH);
        return getDateCreated().format(formatter);
    }
}
