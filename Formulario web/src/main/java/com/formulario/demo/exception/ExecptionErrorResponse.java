package com.formulario.demo.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ExecptionErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final String trace;

    public ExecptionErrorResponse(int status, String error, String message, String trace) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.trace = trace;
    }


}

