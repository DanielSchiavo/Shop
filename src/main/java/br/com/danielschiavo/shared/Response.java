package br.com.danielschiavo.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
}
