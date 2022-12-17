package am.itspace.sweetbakerystorerest.exception;

public class BaseException extends Exception {

    private Error error;

    public BaseException(Error error) {
        this.error = error;
    }


}
