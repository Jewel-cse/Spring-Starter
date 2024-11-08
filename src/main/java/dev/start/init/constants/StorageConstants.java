package dev.start.init.constants;

public final class StorageConstants {
    public  static  final String PATH_CANNOT_BE_NULL= "File path can't be null";
    public  static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB

    private StorageConstants() {
        throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
    }
}
