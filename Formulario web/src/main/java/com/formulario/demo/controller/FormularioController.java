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

    @CrossOrigin(value = "*")
    @PostMapping("/formulario-create")
    private Formulario create(@RequestBody Formulario formulario) {
        return formularioService.create(formulario);
    }

    @CrossOrigin(value = "*")
    @PutMapping("/formulario-update")
    private Formulario update(@RequestBody Formulario formulario) {
        return formularioService.update(formulario);
    }

    @CrossOrigin(value = "*")
    @DeleteMapping("/formulario-delete/{id}")
    private void delete(@PathVariable Long id) {

        formularioService.delete(id);
    }

    @CrossOrigin(value = "*")
    @GetMapping("/read")
    private Page<Formulario> read(@RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "pageNumber") Integer pageNumber) {
        return formularioService.read(pageSize, pageNumber);

    }

    @CrossOrigin(value = "*")
    @GetMapping("/filtrar")
    public ResponseEntity<List<Formulario>> filtrarFormularios(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciiu,
            @RequestParam(required = false) String sucursal,
            @RequestParam(required = false) String email) {

        List<Formulario> formularios = formularioService.listaFiltros(codigo, nombre, ciiu, sucursal, email);

        return new ResponseEntity<>(formularios, HttpStatus.OK);
    }
    @CrossOrigin(value = "*")
    @PostMapping("/cargar-csv")
    public ResponseEntity<String> cargarDesdeCSV(@RequestParam("archivo") MultipartFile archivo) {
        try {
            formularioService.cargarDesdeCSV(archivo);
            return ResponseEntity.ok("Carga de datos exitosa");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cargar datos desde el archivo CSV: " + e.getMessage());
        }
    }
    @CrossOrigin(value = "*")
    @GetMapping("/todos")
    public List<Formulario> getAllFormularios() {
        return formularioService.getAllFormularios();
    }


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
    @CrossOrigin(value = "*")
    @GetMapping("/export-to-excel")
    public EcxelResponseDTO exportToExcel() {
        return formularioService.exportToExcel();
    }


}