package com.formulario.demo.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
    public class FormularioValidationException extends RuntimeException {
        private final int status;
        private final String error;
        private final String trace;

        public FormularioValidationException(String message, int status, String error, String trace) {
            super(message);
            this.status = status;
            this.error = error;
            this.trace = trace;
        }


    }



