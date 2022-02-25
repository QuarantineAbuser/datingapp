package mk.ukim.finki.datingapp.models.exceptions;

public class InvalidPostException extends RuntimeException{

    public InvalidPostException(){
        super("You must enter something!");
    }
}