package br.com.danielschiavo.filestorage.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class FileReferenceKey implements Serializable {

    private String directory;

    private String fileName;

    public FileReferenceKey(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
    }

    public String getPath() {
        return directory + "/" + fileName;
    }
}
