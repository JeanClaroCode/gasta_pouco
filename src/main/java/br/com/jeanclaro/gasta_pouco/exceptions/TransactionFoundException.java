package br.com.jeanclaro.gasta_pouco.exceptions;

public class TransactionFoundException extends RuntimeException{
    public TransactionFoundException(){
        super("Transaction Not Found");
    }
}
