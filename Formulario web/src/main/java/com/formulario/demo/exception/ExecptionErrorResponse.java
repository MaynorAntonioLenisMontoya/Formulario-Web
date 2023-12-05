package com.formulario.demo.exception;

import lombok.Builder;
import lombok.Data;

/*
    la clase ExecptionErrorResponse se utiliza para representar información
    estructurada sobre errores en una aplicación
 */

@Data
@Builder

public class ExecptionErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final String trace;

    /*
    Un constructor público que acepta los valores necesarios para inicializar
    los campos de la clase. La inmutabilidad  se logra al declarar los campos como final y
    asignarles valores solo a través del constructor.
     */
    public ExecptionErrorResponse(int status, String error, String message, String trace) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.trace = trace;
    }


}

