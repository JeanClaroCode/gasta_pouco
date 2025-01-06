package br.com.jeanclaro.gasta_pouco.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(){
        super("Transaction Not Found");
    }
}
