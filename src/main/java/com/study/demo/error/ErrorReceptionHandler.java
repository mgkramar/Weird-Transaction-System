package com.study.demo.error;

import com.study.demo.dto.BaseErrorResponseDTO;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.AmountSmallerThanMinimumException;
import com.study.demo.exception.DuplicatedTransactionException;
import com.study.demo.exception.IpAdressNotKnownException;
import com.study.demo.exception.SameEntityException;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.exception.TransactionNotFoundException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorReceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ SenderNotValidException.class, IpAdressNotKnownException.class })
    protected ResponseEntity<?> handleSenderNotValidConflict(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.UNAUTHORIZED, "IP address error", ex.getMessage()));
    }

    @ExceptionHandler(DuplicatedTransactionException.class)
    protected ResponseEntity<?> handleDuplicatedTransactionConflict(DuplicatedTransactionException ex,
            WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST, "Duplicated transaction", ex.getMessage()));
    }

    @ExceptionHandler(SameEntityException.class)
    protected ResponseEntity<?> handleSameEntityConflict(SameEntityException ex, WebRequest request) {
        return buildResponseEntity(new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST,
                "Sender and receiver pertain to the same entity", ex.getMessage()));
    }

    @ExceptionHandler({ AmountGreaterThanMaximumException.class, AmountSmallerThanMinimumException.class })
    protected ResponseEntity<?> handleAmountBoundariesConflict(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST,
                "Transaction amount outside of the limits", ex.getMessage()));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    protected ResponseEntity<?> handleNotExistingTransaction(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST, "Transaction not found", ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String fieldName = ex.getBindingResult().getFieldError().getField();
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

        return buildResponseEntity(new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST, "Message body failed validation",
                "Field {" + fieldName + "} " + errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String message = "The value {" + ex.getValue()
                + "} is not valid. Check the query parameters / headers / path variables to make sure you pass valid values";
        return buildResponseEntity(new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST, "Value not valid", message));
    }

    private ResponseEntity<Object> buildResponseEntity(BaseErrorResponseDTO error) {
        return ResponseEntity.status(error.getStatus()).contentType(MediaType.APPLICATION_JSON).body(error);
    }
}