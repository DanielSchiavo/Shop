package br.com.danielschiavo.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Response {

    private LocalDateTime timestamp;
    private String message;
    private Object data;
    private List<Object> details;

    public static Response success(String message, Object data) {
        return new Response(LocalDateTime.now(), message, data, null);
    }

    public static Response failure(String message, List<Object> details) {
        return new Response(LocalDateTime.now(), message, null, details );
    }

    public static <K, V> Response failureMap(String message, Map<K, V> details) {
        List<Object> dto = new ArrayList<>();
        details.forEach((key, value) -> dto.add(Map.of(key, value)));
        return new Response(LocalDateTime.now(), message, null, dto);
    }
}
