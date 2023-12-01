package com.formulario.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Formulario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
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
