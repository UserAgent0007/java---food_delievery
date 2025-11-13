package lab.exceptions;


public class CustomerException extends RuntimeException{
    
    public CustomerException(){
        super();
    }

    public CustomerException(String message){
        super(message);
    }

    public CustomerException(Throwable e){
        super(e);
    }

    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
