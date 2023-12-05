package com.formulario.demo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*
 se utiliza para transportar la respuesta de la exportación de datos a un formato Excel.
 Contiene un solo campo, b64, que almacena la representación Base64 del archivo Excel.
 */
public class EcxelResponseDTO {

    private String b64;
}
