package br.com.danielschiavo.filestorage.mapper;

import br.com.danielschiavo.filestorage.dto.request.CreateBucketRequest;
import br.com.danielschiavo.filestorage.dto.response.ShowBucketResponse;
import br.com.danielschiavo.filestorage.model.Bucket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface BucketMapper {

    @Mapping(target = "createdByAdminId", source = "customerId")
    @Mapping(target = "creationDateTime", expression = "java(LocalDateTime.now())")
    Bucket toEntity(CreateBucketRequest request, Long customerId);

    ShowBucketResponse toDto(Bucket bucket);
}
