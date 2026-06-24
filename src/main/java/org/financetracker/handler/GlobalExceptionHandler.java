package org.financetracker.handler;

import org.financetracker.dto.ErrorResponseDTO;
import org.financetracker.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

//@RestControllerAdvice - Перехватывает все ошибки из контроллеров и обрабатывает их здесь
@RestControllerAdvice
public class GlobalExceptionHandler {
    private String getPath(WebRequest request) {
        String path;
        try {
            String description = request.getDescription(false);
            path = description.replace("\\s", "");
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        return path;
    }
    //404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException exception, WebRequest request) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .date(LocalDate.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .path(getPath(request))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {

    Map<String, String> validationErrors = exception.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
            error -> error.getField(),              // ключ
            error -> error.getDefaultMessage()));              //значение(сообщение)

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .date(LocalDate.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation Failed")
                .path(getPath(request))
                .validationErrors(validationErrors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception exception, WebRequest request) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .date(LocalDate.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(exception.getMessage())
                .path(getPath(request))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}