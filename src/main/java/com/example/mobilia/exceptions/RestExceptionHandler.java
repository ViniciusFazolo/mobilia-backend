package com.example.mobilia.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PadraoException.class)
    public ResponseEntity<ErroPadrao> PadraoExceptionHandler(PadraoException e, HttpServletRequest req) {
        ErroPadrao err = new ErroPadrao();
        err.setMessage(e.getMessage());
        err.setPath(req.getRequestURI());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setTimestamp(Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<ErroPadrao>
    // handleAccessDeniedException(AccessDeniedException e, HttpServletRequest req)
    // {
    // ErroPadrao err = new ErroPadrao();
    // err.setMessage("Você não tem permissão para acessar este recurso.");
    // err.setPath(req.getRequestURI());
    // err.setStatus(HttpStatus.FORBIDDEN.value());
    // err.setTimestamp(Instant.now());
    // return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    // }
}
