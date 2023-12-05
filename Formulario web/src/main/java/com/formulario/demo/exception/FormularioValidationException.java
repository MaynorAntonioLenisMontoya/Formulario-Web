package com.formulario.demo.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
  FormularioValidationException es una excepción específica de validación
  para la clase Formulario, diseñada para manejar situaciones donde se detectan errores de validación.
  La anotación @ResponseStatus indica que se debería asociar un código de estado HTTP 400 (Bad Request)
  cuando se lanza esta excepción. Los campos inmutables y el constructor
  permiten proporcionar detalles adicionales sobre el error al capturar esta excepción.
 */

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



