package mk.ukim.finki.datingapp.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(Long id) {
        super(String.format("User with id '%d' was not found!", id));
    }
}
