package com.formulario.demo.Repository;


import com.formulario.demo.model.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository <Formulario, Long> {


  @Query(value = """
    SELECT * FROM formulario 
    WHERE (:codigo IS NULL OR formulario.codigo = :codigo)
       AND (:nombre IS NULL OR formulario.nombre = :nombre)
       AND (:ciiu IS NULL OR formulario.ciiu = :ciiu)
       AND (:sucursal IS NULL OR formulario.sucursal = :sucursal)
       AND (:email IS NULL OR formulario.email = :email);
""", nativeQuery = true)
  List<Formulario> listaFiltros(
          @Param("codigo") String codigo,
          @Param("nombre") String nombre,
          @Param("ciiu") String ciiu,
          @Param("sucursal") String sucursal,
          @Param("email") String email
  );
  

}