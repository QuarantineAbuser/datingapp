package mk.ukim.finki.datingapp.models.exceptions;

public class InvalidArgumentsException extends RuntimeException{
    public InvalidArgumentsException(String argument){
        super(String.format("Please enter valid %s!", argument));
    }
}
