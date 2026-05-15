package astr.api.controller.exceptionhandler;


import astr.api.clientconfig.exception.NasaApiException;
import astr.api.service.exception.InvalidDateRangeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDateRange(

            InvalidDateRangeException exception,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(

                LocalDateTime.now(),

                HttpStatus.BAD_REQUEST.value(),

                "Invalid request",

                exception.getMessage(),

                request.getRequestURI());

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(NasaApiException.class)
    public ResponseEntity<ApiErrorResponse> handleNasaApiException(

            NasaApiException exception,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(

                LocalDateTime.now(),

                HttpStatus.SERVICE_UNAVAILABLE.value(),

                "NASA API unavailable",

                exception.getMessage(),

                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(

            Exception exception,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(

                LocalDateTime.now(),

                HttpStatus.INTERNAL_SERVER_ERROR.value(),

                "Internal server error",

                exception.getMessage(),

                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
