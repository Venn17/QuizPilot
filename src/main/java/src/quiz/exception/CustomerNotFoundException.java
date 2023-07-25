package src.quiz.exception;

public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return " " + super.getMessage();
    }
}