package com.example.demowithtests.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.SendFailedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(404), ex.getMessage(), "There is no such resource", request.getDescription(true));
    }

    @ExceptionHandler(ResourceWasDeletedException.class)
    public ProblemDetail handleDeleteException(ResourceWasDeletedException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(404), ex.getMessage(), "This user was deleted", request.getDescription(true));
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail globleExcpetionHandler(Exception ex, WebRequest request) {
        System.out.println("Exception: " + ex.getMessage());
        return createProblemDetail(HttpStatusCode.valueOf(400), ex.getMessage(), "A global exception occurred", request.getDescription(false));
    }

    @ExceptionHandler(SendFailedException.class)
    public ProblemDetail methodArgumentNotValidException(SendFailedException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(400), ex.getMessage(), "Mail sending failed", request.getDescription(false));
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ProblemDetail methodAuthenticationFailedException(AuthenticationFailedException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(401), ex.getMessage(), "Mail authentication failed exception", request.getDescription(false));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(400), ex.getMessage(), "Email is not unique exception", request.getDescription(false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(400), ex.getMessage(), "Illegal argument exception", request.getDescription(false));
    }

    @ExceptionHandler(FieldMissingException.class)
    public ProblemDetail handleFieldMissingException(FieldMissingException ex, WebRequest request) {
        return createProblemDetail(HttpStatusCode.valueOf(400), ex.getMessage(), "Field missing exception", request.getDescription(false));
    }


    private ProblemDetail createProblemDetail(HttpStatusCode status, String message, String title, String requestDescription) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setTitle(title);
        pd.setProperty("timestamp", new Date());
        pd.setProperty("requestDescription", requestDescription);
        return pd;
    }

    @Data
    @AllArgsConstructor
    private static class MyGlobalExceptionHandler {
        private String message;
    }
}
