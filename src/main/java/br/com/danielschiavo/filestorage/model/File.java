package br.com.danielschiavo.filestorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class File {

    private String fileName;

    private byte[] content;
}
