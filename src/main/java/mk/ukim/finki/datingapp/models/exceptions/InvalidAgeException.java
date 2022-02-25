package mk.ukim.finki.datingapp.models.exceptions;

public class InvalidAgeException extends RuntimeException{
    public InvalidAgeException(){
        super("You must be 18+ to register!");
    }
}
