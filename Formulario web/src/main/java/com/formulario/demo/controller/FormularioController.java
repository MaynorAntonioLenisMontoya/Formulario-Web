package com.formulario.demo.controller;

import ch.qos.logback.core.model.Model;
import com.formulario.demo.dto.FormularioRequestDTO;
import com.formulario.demo.dto.response.EcxelResponseDTO;
import com.formulario.demo.model.Formulario;
import com.formulario.demo.service.FormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/formularios")
public class FormularioController {
    @Autowired
    private FormularioService formularioService;

    /*
    este controlador define un punto de entrada para crear formularios.
    A través de solicitudes POST a "/formulario-create", se espera que se
    proporcione datos del formulario en el cuerpo de la solicitud.
    Luego, estos datos se utilizan para crear un nuevo formulario utilizando un servicio.
     */
    @CrossOrigin(value = "*")
    @PostMapping("/formulario-create")
    private Formulario create(@RequestBody Formulario formulario) {
        return formularioService.create(formulario);
    }
    /*
      este controlador define un punto de entrada para actualizar formularios.
      A través de solicitudes POST a "/formulario-update", se espera que se
      proporcione datos del formulario en el cuerpo de la solicitud.
      Luego, estos datos se utilizan para actualizar un nuevo formulario utilizando un servicio.
       */
    @CrossOrigin(value = "*")
    @PutMapping("/formulario-update")
    private Formulario update(@RequestBody Formulario formulario) {
        return formularioService.update(formulario);
    }

    /*
    este controlador define un endpoint DELETE ("/formulario-delete/{id}")
    que permite eliminar un formulario específico.
     */
    @CrossOrigin(value = "*")
    @DeleteMapping("/formulario-delete/{id}")
    private void delete(@PathVariable Long id) {

        formularioService.delete(id);
    }
    /*
     Este controlador permite recuperar una página de formularios.
   */
    @CrossOrigin(value = "*")
    @GetMapping("/read")
    private Page<Formulario> read(@RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "pageNumber") Integer pageNumber) {
        return formularioService.read(pageSize, pageNumber);

    }

    /*
     este controlador define un endpoint GET ("/filtrar")
     que permite filtrar formularios basados en diferentes criterios proporcionados como parámetros de consulta.
     */
    @CrossOrigin(value = "*")
    @GetMapping("/filtrar")
    public ResponseEntity<List<Formulario>> filtrarFormularios(
            /*
            estos parámetros de consulta permiten filtrar formularios basados en
            criterios específicos, como el código, nombre, ciiu, sucursal y email,
            required = false:Este atributo indica que el parámetro es opcional.
            Si no se proporciona en la solicitud, el valor predeterminado es null.
             */
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciiu,
            @RequestParam(required = false) String sucursal,
            @RequestParam(required = false) String email) {

        /*
        Este código invoca el método listaFiltros en el servicio formularioService,
        pasando los criterios de filtrado como parámetros
         */
        List<Formulario> formularios = formularioService.listaFiltros(codigo, nombre, ciiu, sucursal, email);

        return new ResponseEntity<>(formularios, HttpStatus.OK);
    }

    /*
    Este método toma un parámetro llamado "archivo",
    que se espera que sea un archivo MultipartFile que contiene los datos CSV a cargar.
     */
    @CrossOrigin(value = "*")
    @PostMapping("/cargar-csv")
    public ResponseEntity<String> cargarDesdeCSV(@RequestParam("archivo") MultipartFile archivo) {
        /*
        Este bloque de código intenta llamar al método cargarDesdeCSV en el servicio
        formularioService y maneja cualquier excepción que pueda ocurrir durante el proceso.
         */
        try {
            formularioService.cargarDesdeCSV(archivo);
            return ResponseEntity.ok("Carga de datos exitosa");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cargar datos desde el archivo CSV: " + e.getMessage());
        }
    }

    /*
    este controlador define un endpoint GET ("/todos")
    que permite recuperar todos los formularios almacenados en el sistema. La anotación
     */
    @CrossOrigin(value = "*")
    @GetMapping("/todos")
    public List<Formulario> getAllFormularios() {
        return formularioService.getAllFormularios();
    }

    /*
        este controlador define un endpoint GET ("/getById/{id}")
        que permite obtener un formulario específico por su identificador.
    */
        @CrossOrigin(value = "*")
        @GetMapping("/getById/{id}")
        public ResponseEntity<Formulario> getFormularioById(@PathVariable Long id) {
            try {
                Formulario formulario = formularioService.findById(id);
                return new ResponseEntity<>(formulario, HttpStatus.OK);
            } catch (RuntimeException e) {
                // Manejar la excepción si el formulario no se encuentra
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        /*
         este controlador define un endpoint GET ("/export-to-excel")
         que permite exportar datos a un archivo Excel.
        */
    @CrossOrigin(value = "*")
    @GetMapping("/export-to-excel")
    public EcxelResponseDTO exportToExcel() {
        return formularioService.exportToExcel();
    }


}