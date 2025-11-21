package lab.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(){
        super();
    }

    public AlreadyExistsException(String message){
        super(message);
    }

    public AlreadyExistsException(Throwable e){
        super(e);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
