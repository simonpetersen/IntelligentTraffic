package exception;

public class WebServiceException extends Exception {

    public WebServiceException(String message, Exception e) {
        super(message, e);
    }

    public WebServiceException(String message) {
        super(message);
    }
}
