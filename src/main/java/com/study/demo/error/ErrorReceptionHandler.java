package com.study.demo.error;

import com.study.demo.dto.BaseErrorResponseDTO;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.AmountSmallerThanMinimumException;
import com.study.demo.exception.DuplicatedTransactionException;
import com.study.demo.exception.SameBankException;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.exception.TransactionNotFoundException;

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

    @ExceptionHandler(SenderNotValidException.class)
    protected ResponseEntity<?> handleSenderNotValidConflict(SenderNotValidException ex, WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.UNAUTHORIZED, "Sender bank not valid", ex.getMessage()));
    }

    @ExceptionHandler(DuplicatedTransactionException.class)
    protected ResponseEntity<?> handleDuplicatedTransactionConflict(DuplicatedTransactionException ex,
            WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST, "Duplicated transaction", ex.getMessage()));
    }

    @ExceptionHandler(SameBankException.class)
    protected ResponseEntity<?> handleSameBankConflict(SameBankException ex, WebRequest request) {
        return buildResponseEntity(new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST,
                "Sender and receiver pertain to the same bank", ex.getMessage()));
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

    private ResponseEntity<Object> buildResponseEntity(BaseErrorResponseDTO error) {
        return ResponseEntity.status(error.getStatus()).contentType(MediaType.APPLICATION_JSON).body(error);
    }
}