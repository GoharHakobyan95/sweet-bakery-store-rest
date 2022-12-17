package am.itspace.sweetbakerystorerest.exception;

public class EntityAlreadyExistsException extends BaseException {

    public EntityAlreadyExistsException(Error error) {
        super(error);
    }
}
