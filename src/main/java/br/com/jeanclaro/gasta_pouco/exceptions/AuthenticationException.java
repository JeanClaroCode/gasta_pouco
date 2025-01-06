package br.com.jeanclaro.gasta_pouco.exceptions;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(){
        super("Email/Password incorrect");
    }
}
