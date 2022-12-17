package am.itspace.sweetbakerystorerest.exception;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(Error error) {
        super(error);
    }

}
