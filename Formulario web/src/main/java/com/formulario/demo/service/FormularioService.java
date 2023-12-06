package com.formulario.demo.service;


import com.formulario.demo.dto.response.EcxelResponseDTO;
import com.formulario.demo.model.Formulario;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FormularioService {

    // Método para crear un nuevo formulario
    Formulario create(Formulario formulario);

    // Método para actualizar un formulario existente
    Formulario update(Formulario formulario);

    // Método para eliminar un formulario por su identificador
    void delete(Long id);

    // Método para obtener una página de formularios
    Page<Formulario> read(Integer pageSize, Integer pageNumber);

    // Método para filtrar formularios según varios criterios
    List<Formulario> listaFiltros(String codigo, String nombre, String ciiu, String sucursal, String email);

    // Método para cargar datos desde un archivo CSV
    void cargarDesdeCSV(MultipartFile archivo) throws IOException;

    // Método para obtener todos los formularios
    List<Formulario> getAllFormularios();

    // Método para encontrar un formulario por su identificador
    Formulario findById(Long id);

    // Método para exportar formularios a un archivo Excel
    EcxelResponseDTO exportToExcel();
}
