package at.technikum.apps.mtcg.exceptions;

public class CustomExceptions extends Throwable{

    public CustomExceptions() {super();}

    public CustomExceptions(String message) {
        super(message);
    }
    public CustomExceptions(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomExceptions(Throwable cause) {
        super(cause);
    }

}
