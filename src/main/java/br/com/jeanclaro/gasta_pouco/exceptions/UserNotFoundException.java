package br.com.jeanclaro.gasta_pouco.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User Not Found");
    }
}
