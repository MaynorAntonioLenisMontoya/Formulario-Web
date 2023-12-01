package com.formulario.demo.service;

import com.formulario.demo.dto.FormularioRequestDTO;
import com.formulario.demo.dto.response.EcxelResponseDTO;
import com.formulario.demo.model.Formulario;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FormularioService {

    Formulario create(Formulario formulario);

    Formulario update(Formulario formulario);

    void delete(Long id);

    Page<Formulario> read(Integer pageSize, Integer pageNumber);


    List<Formulario> listaFiltros(String codigo, String nombre, String ciiu, String sucursal, String email);

    void cargarDesdeCSV(MultipartFile archivo) throws IOException;

    List<Formulario> getAllFormularios();

    Formulario findById(Long id);

    EcxelResponseDTO exportToExcel();


}
