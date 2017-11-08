package exception;

public class WebServiceException extends Exception {

    public WebServiceException(Exception e, String message) {
        super(message, e);
    }

    public WebServiceException(String message) {
        super(message);
    }
}
