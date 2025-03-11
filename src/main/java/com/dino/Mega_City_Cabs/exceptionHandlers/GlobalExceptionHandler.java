package com.dino.Mega_City_Cabs.exceptionHandlers;

import com.dino.Mega_City_Cabs.enums.RestApiResponseStatusCodes;
import com.dino.Mega_City_Cabs.utils.ErrorCodes;
import com.dino.Mega_City_Cabs.utils.ErrorDetail;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Handles global exceptions and provides custom error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorCodes errorCodes;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDetail>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errorDetails = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            ErrorDetail errorDetail = new ErrorDetail(new Date(), "Invalid value for " + fieldName + ": " + errorMessage, errorCodes.getInputNotValid());
            errorDetails.add(errorDetail);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseWrapper<?>> handleDataIntegrityViolationExceptions(
            DataIntegrityViolationException e) {
        if (e.getMessage().contains("foreign key constraint fails")) {
            return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                    ValidationMessages.FOREIGN_KEY_CONSTRIN, null));
        }

        int start = e.getMessage().indexOf("[") + 1;
        int end = e.getMessage().indexOf("]", start);
        if (start > 0 && end > start) {
            return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                    e.getMessage().substring(start, end), e.getMessage()));
        }
        return ResponseEntity.ok(
                new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(), "Unknown Error", e.getMessage()));

    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseWrapper<?>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                ValidationMessages.INVALID_ID, e.toString()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseWrapper<?>> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                ValidationMessages.WRONG_API_CALL, e.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                ValidationMessages.WRONG_API_CALL, null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseWrapper<?>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                ValidationMessages.MISMATCH_INPUT, null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseWrapper<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseWrapper<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(), e.getMessage(), e));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<?>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity
                .ok(new ResponseWrapper<>(RestApiResponseStatusCodes.NOT_FOUND.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseWrapper<?>> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                ValidationMessages.INVALID_CREDENTIAL, e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseWrapper<?>> handleConstraintViolationException(ConstraintViolationException e) {
        // Extract messages and remove text before the first colon
        String errorMessages = e.getConstraintViolations().stream().map(violation -> {
            String message = violation.getMessage();
            int index = message.indexOf(':');
            if (index != -1) {
                message = message.substring(index + 1).trim();
            }
            return message;
        }).collect(Collectors.joining(" ")); // Join messages with space

        return ResponseEntity
                .ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(), errorMessages, null));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ResponseWrapper<?>> handleBadCredentialsException(TransactionSystemException e) {
        return ResponseEntity.ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                ValidationMessages.MIN_REQUIREMENT, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<?>> handleOtherExceptions(Exception e) {
        return ResponseEntity
                .ok(new ResponseWrapper<>(RestApiResponseStatusCodes.BAD_REQUEST.getCode(), e.getMessage(), e));
    }

    // Add more exception handlers as needed

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ResponseWrapper<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .reduce("", (acc, curr) -> acc + curr + "; ");
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
//                RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
//                errorMessage,
//                null
//        ));
//    }
}