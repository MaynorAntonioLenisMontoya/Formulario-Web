package com.formulario.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FormularioValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExecptionErrorResponse> handleFormularioValidationException(FormularioValidationException ex) {
        ExecptionErrorResponse execptionErrorResponse = new ExecptionErrorResponse(
                ex.getStatus(),
                ex.getError(),
                ex.getMessage(),
                ex.getTrace()
        );
        return new ResponseEntity<>(execptionErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
