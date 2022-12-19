package runanalyst.storage;

public class StorageException extends Exception {
    public StorageException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public StorageException(String errorMessage) {
        super(errorMessage);
    }
}
