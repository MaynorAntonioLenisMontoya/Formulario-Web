package com.formulario.demo.Repository;


import com.formulario.demo.model.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/*
  La interfaz FormularioRepository, que extiende la interfaz JpaRepository
  proporcionada por Spring Data JPA, actúa como una interfaz de repositorio
  en una aplicación Java basada en el framework Spring. Esta interfaz es responsable
  de gestionar la interacción entre la aplicación y la capa de persistencia, específicamente
  para la entidad Formulario en el contexto de una base de datos relacional.
 */
@Repository
public interface FormularioRepository extends JpaRepository <Formulario, Long> {



  /*IS NULL OR :Si el valor del parámetro es nulo,
   esa parte de la condición no limita los resultados.
   Si el valor del parámetro está presente, la condición afecta la búsqueda según ese valor.

    este verifica si el "atributo" es nulo si este no es
    nulo compara el valor.
    ejemplo:
    "codigo" si este valor no es nulo lo compara con el valor de
    "formulario.codigo" con el valor de "codigo"
   */
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
          /*
          El @Param permite  indica que el parametro que se esta pasando
          se asosia con el parametro que este en la consultaSQL
          Esta anotación proporciona un mecanismo de enlace entre los parámetros en
          la interfaz del método y los parámetros en la consulta.
          ejemplo:
          @Param("email") indica que el parámetro "email" en el método "listaFiltros" está
          asociado con el parámetro de la consulta SQL que lleva el mismo nombre, es decir, ":email."
          */
  );
  

}