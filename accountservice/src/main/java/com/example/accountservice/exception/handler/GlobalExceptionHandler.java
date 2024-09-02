package com.example.accountservice.exception.handler;

import com.example.accountservice.exception.*;
import com.example.accountservice.model.common.CustomError;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    subErrors.add(
                            CustomError.CustomSubError.builder()
                                    .field(fieldName)
                                    .message(message)
                                    .build()
                    );
                }
        );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException constraintViolationException) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();
        constraintViolationException.getConstraintViolations()
                .forEach(constraintViolation ->
                        subErrors.add(
                                CustomError.CustomSubError.builder()
                                        .message(constraintViolation.getMessage())
                                        .field(StringUtils.substringAfterLast(constraintViolation.getPropertyPath().toString(), "."))
                                        .value(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null)
                                        .type(constraintViolation.getInvalidValue().getClass().getSimpleName())
                                        .build()
                        )
                );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(final RuntimeException runtimeException) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(runtimeException.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordNotValidException.class)
    protected ResponseEntity<CustomError> handlePasswordNotValidException(final PasswordNotValidException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenAlreadyInvalidatedException.class)
    protected ResponseEntity<Object> handleTokenAlreadyInvalidatedException(final TokenAlreadyInvalidatedException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(final UserAlreadyExistException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(final UserNotFoundException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserStatusNotValidException.class)
    protected ResponseEntity<Object> handleUserStatusNotValidException(final UserStatusNotValidException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BrandAlreadyExistException.class)
    protected ResponseEntity<Object> handleBrandAlreadyExistException(final BrandAlreadyExistException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    protected ResponseEntity<Object> handleBrandNotFoundException(final BrandNotFoundException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }
}
