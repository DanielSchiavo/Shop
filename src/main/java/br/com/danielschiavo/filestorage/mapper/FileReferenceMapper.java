package br.com.danielschiavo.filestorage.mapper;

import br.com.danielschiavo.filestorage.dto.request.UploadObjectRequest;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import br.com.danielschiavo.filestorage.model.FileReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileReferenceMapper {

    FileReference toEntity(UploadObjectRequest request);

    @Mapping(target = "directory", source = "fileReference.file.directory")
    @Mapping(target = "fileName", source = "fileReference.file.fileName")
    DetailFileReferenceResponse toDtoDetail(FileReference fileReference);

    List<DetailFileReferenceResponse> toDtoDetail(List<FileReference> filesReferences);

    @Mapping(target = "file.directory", source = "destDirectory")
    FileReference copyFileReference(FileReference fileReference, String destDirectory);
}
