package br.com.danielschiavo.filestorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    DOCUMENT(false),
    IMAGE(true);

    private final boolean publicAccessible;
}