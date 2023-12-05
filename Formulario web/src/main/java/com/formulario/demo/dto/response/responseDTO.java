package com.formulario.demo.dto.response;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*
 se utiliza para encapsular información relacionada con una respuesta,
 como mensajes, información de error y un identificador
 */
public class responseDTO {

        private String mensaje;

        private Long error;

        private  Long id;


}
