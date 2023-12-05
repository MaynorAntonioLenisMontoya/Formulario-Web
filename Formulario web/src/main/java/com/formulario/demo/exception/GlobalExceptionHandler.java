package com.formulario.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

    /*
        GlobalExceptionHandler está diseñada para manejar globalmente las excepciones
        del tipo FormularioValidationException. Cuando se lanza esta excepción en cualquier
        parte de la aplicación, este manejador se activará y devolverá una respuesta HTTP con detalles
        personalizados del error, utilizando la clase ExecptionErrorResponse para encapsular la información
        del error. Esto contribuye a la coherencia y reutilización del código de manejo
        de excepciones en toda la aplicación
    */
@ControllerAdvice
public class GlobalExceptionHandler {
    /*
        Esta anotación se aplica al método handleFormularioValidationException y especifica
        que este método manejará excepciones del tipo FormularioValidationException.
      */
    @ExceptionHandler(FormularioValidationException.class)
   /*
      Esta anotación se aplica al método handleFormularioValidationException y especifica
      que este método manejará excepciones del tipo FormularioValidationException.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    /*
      Este método maneja las excepciones del tipo FormularioValidationException. Recibe un objeto
      de esa excepción como argumento
    */
    public ResponseEntity<ExecptionErrorResponse> handleFormularioValidationException(FormularioValidationException ex) {
    /*
      Se crea una instancia de ExecptionErrorResponse utilizando la información de la excepción capturada
    */
        ExecptionErrorResponse execptionErrorResponse = new ExecptionErrorResponse(
                ex.getStatus(),
                ex.getError(),
                ex.getMessage(),
                ex.getTrace()
        );

    /*
        Se devuelve una respuesta HTTP con el objeto
    */
        return new ResponseEntity<>(execptionErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
