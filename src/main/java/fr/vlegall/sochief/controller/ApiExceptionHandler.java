package fr.vlegall.sochief.controller;

import fr.vlegall.sochief.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage() != null ? ex.getMessage() : "Not found");
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad request");
        pd.setDetail(ex.getMessage() != null ? ex.getMessage() : "Invalid request");
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad request");
        pd.setDetail("Invalid value for parameter '" + ex.getName() + "'");
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation error");
        pd.setDetail("Request validation failed");
        pd.setProperty("path", req.getRequestURI());

        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(it -> Map.of(
                        "field", it.getField(),
                        "message", it.getDefaultMessage() != null ? it.getDefaultMessage() : "invalid",
                        "rejectedValue", it.getRejectedValue()
                ))
                .collect(Collectors.toList());
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation error");
        pd.setDetail("Constraint violations");
        pd.setProperty("path", req.getRequestURI());
        List<Map<String, Object>> errors = ex.getConstraintViolations().stream()
                .map(cv -> Map.of(
                        "property", cv.getPropertyPath().toString(),
                        "message", cv.getMessage(),
                        "invalidValue", cv.getInvalidValue()
                ))
                .collect(Collectors.toList());
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Data integrity violation");
        String detail = ex.getRootCause() != null && ex.getRootCause().getMessage() != null
                ? ex.getRootCause().getMessage()
                : (ex.getMessage() != null ? ex.getMessage() : "Conflict with database constraints");
        pd.setDetail(detail);
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleInternalError(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal server error");
        pd.setDetail("An unexpected error occurred");
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }
}

