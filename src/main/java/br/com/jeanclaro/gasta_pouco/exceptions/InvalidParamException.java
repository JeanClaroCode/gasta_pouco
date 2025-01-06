package br.com.jeanclaro.gasta_pouco.exceptions;

public class InvalidParamException extends RuntimeException {
    public InvalidParamException(){
        super("Invalid input data");
    }
}
