package exception; // Make sure this matches the package of your TaskManager

public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}