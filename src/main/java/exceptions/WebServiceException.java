package exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebServiceException extends Exception {

    private String errorMessage;

    public WebServiceException(Exception e, String message) {
        super(e);
        this.errorMessage = message;
    }

    public WebServiceException(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
