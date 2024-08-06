package br.com.danielschiavo.filestorage.exception;

import lombok.Getter;

import java.util.Map;

public class FileStorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    @Getter
    private Map<String, String> details;

	public FileStorageException(String message, Map<String, String> details) {
        super(message);
        this.details = details;
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStorageException(String message) {
        super(message);
    }
}
