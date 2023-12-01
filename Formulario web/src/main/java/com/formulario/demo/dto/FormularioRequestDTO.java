package com.formulario.demo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class FormularioRequestDTO {

    private Long id;

    private String codigo;

    private String sucursal;

    private String nit;

    private String nit_dv;

    private int tipo_terc;

    private int ind_rut;

    private String nombre;

    private String nombre_establec;

    private int tipo_ident;

    private String estado;

    private int pais;

    private int dpto;

    private int ciudad;

    private String email;

    private String barrio;

    private String telefono_2;

    private String ciiu;


}
