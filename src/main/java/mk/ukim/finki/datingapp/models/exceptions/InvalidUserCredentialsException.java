package mk.ukim.finki.datingapp.models.exceptions;

public class InvalidUserCredentialsException extends RuntimeException{

    public InvalidUserCredentialsException(){
        super("Invalid user credentials exception");
    }
}
