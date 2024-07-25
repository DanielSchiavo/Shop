package br.com.danielschiavo.filestorage.mapper;

import br.com.danielschiavo.filestorage.dto.response.FileResponse;
import br.com.danielschiavo.filestorage.model.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileResponse toDto(File file);

}
