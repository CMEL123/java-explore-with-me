package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private final String reason;
    private final HttpStatus status;
    private final LocalDateTime timestamp;
}
