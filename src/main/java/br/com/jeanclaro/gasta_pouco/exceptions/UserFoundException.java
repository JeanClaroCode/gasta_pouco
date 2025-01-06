package br.com.jeanclaro.gasta_pouco.exceptions;

public class UserFoundException extends RuntimeException {
    public UserFoundException(){
        super("User already exists");
    }

}
