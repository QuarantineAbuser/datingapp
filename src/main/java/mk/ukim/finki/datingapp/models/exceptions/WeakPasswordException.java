package mk.ukim.finki.datingapp.models.exceptions;

public class WeakPasswordException extends RuntimeException{

    public WeakPasswordException() {
        super("Your password must contain at least 5 characters!");
    }
}