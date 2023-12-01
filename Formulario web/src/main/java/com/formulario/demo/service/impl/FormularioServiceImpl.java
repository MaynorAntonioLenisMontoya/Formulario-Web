package com.formulario.demo.service.impl;

import com.formulario.demo.Repository.FormularioRepository;
import com.formulario.demo.dto.response.EcxelResponseDTO;
import com.formulario.demo.exception.FormularioValidationException;
import com.formulario.demo.model.Formulario;
import com.formulario.demo.service.FormularioService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class FormularioServiceImpl implements FormularioService {

    @Autowired
    private FormularioRepository formularioRepository;

    public Formulario create(Formulario formulario) {
        validateFormulario(formulario); // Lanza una excepción si la validación falla
        return formularioRepository.save(formulario);
    }

    @Override
    public Formulario update(Formulario formulario) {
        Formulario updateFormulario = formularioRepository.save(formulario);
        return updateFormulario;
    }

    @Override
    public void delete(Long id) {
        formularioRepository.deleteById(id);
    }

    @Override
    public Page<Formulario> read(Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return formularioRepository.findAll(pageable);
    }

    @Override
    public List<Formulario> listaFiltros(String codigo, String nombre, String ciiu, String sucursal, String email) {
        List<Formulario> resultados = formularioRepository.listaFiltros(codigo, nombre, ciiu, sucursal, email);

        if (resultados.isEmpty()) {
            throw new FormularioValidationException(
                    "No se encontro consulta",
                    404,
                    "Not found",
                    "");
        }

        return resultados;
    }

    @Override
    public void cargarDesdeCSV(MultipartFile archivo) throws IOException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            String[] linea;
            boolean primeraLinea = true;

            while ((linea = csvReader.readNext()) != null) {
                if (primeraLinea) {
                    // Omitir la primera línea que contiene los títulos
                    primeraLinea = false;
                    continue;
                }
                    System.out.println(Arrays.toString(linea));
                // Aquí debes parsear los datos de la línea y crear un objeto Formulario
                Formulario formulario = crearFormularioDesdeLineaCSV(linea);

                // Luego, puedes llamar al método existente para guardar en la base de datos
                create(formulario);
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Formulario> getAllFormularios() {
        return formularioRepository.findAll();
    }

    @Override
    public Formulario findById(Long id) {
        return formularioRepository.findById(id).get();
    }

    @Override
    public EcxelResponseDTO exportToExcel() {

        List<Formulario> result = formularioRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Formularios");

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Codigo", "Sucursal", "Nit", "Nit DV", "Tipo Terc", "Ind RUT",
                    "Nombre", "Nombre Establecimiento", "Tipo Ident", "Estado", "Pais", "Dpto", "Ciudad",
                    "Email", "Barrio", "Telefono 2", "CIIU"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Datos
            for (int rowNum = 1; rowNum <= result.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                Formulario formulario = result.get(rowNum - 1);

                row.createCell(1).setCellValue(formulario.getCodigo());
                row.createCell(2).setCellValue(formulario.getSucursal());
                row.createCell(3).setCellValue(formulario.getNit());
                row.createCell(4).setCellValue(formulario.getNit_dv());
                row.createCell(5).setCellValue(formulario.getTipo_terc());
                row.createCell(6).setCellValue(formulario.getInd_rut());
                row.createCell(7).setCellValue(formulario.getNombre());
                row.createCell(8).setCellValue(formulario.getNombre_establec());
                row.createCell(9).setCellValue(formulario.getTipo_ident());
                row.createCell(10).setCellValue(formulario.getEstado());
                row.createCell(11).setCellValue(formulario.getPais());
                row.createCell(12).setCellValue(formulario.getDpto());
                row.createCell(13).setCellValue(formulario.getCiudad());
                row.createCell(14).setCellValue(formulario.getEmail());
                row.createCell(15).setCellValue(formulario.getBarrio());
                row.createCell(16).setCellValue(formulario.getTelefono_2());
                row.createCell(17).setCellValue(formulario.getCiiu());
            }

            // Convertir el libro de trabajo a un arreglo de bytes
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                byte[] excelBytes = bos.toByteArray();
                // Convertir los bytes a Base64
                return EcxelResponseDTO.builder()
                        .b64(Base64.getEncoder().encodeToString(excelBytes))
                        .build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Formulario crearFormularioDesdeLineaCSV(String[] linea) {
        // Aquí debes mapear los elementos del array a los campos del objeto Formulario
        System.out.println("crearFormularioDesdeLineaCSV "+Arrays.toString(linea));
        String[] data = Arrays.toString(linea).split(";");
        System.out.println("------ "+data.length);
        if (data.length < 17) {
            throw new IllegalArgumentException("La línea del CSV no tiene suficientes elementos");
        }
        Formulario formulario = new Formulario();
        formulario.setCodigo(data[0]); // Ajusta según la estructura de tu CSV
        formulario.setSucursal(data[1]);
        formulario.setNit(data[2]);
        formulario.setNit_dv(data[3]);
        formulario.setTipo_terc(Integer.parseInt(data[4]));
        formulario.setInd_rut(Integer.parseInt(data[5]));
        formulario.setNombre(data[6]);
        formulario.setNombre_establec(data[7]);
        formulario.setTipo_ident(Integer.parseInt(data[8]));
        formulario.setEstado(data[9]);
        formulario.setPais(Integer.parseInt(data[10]));
        formulario.setDpto(Integer.parseInt(data[11]));
        formulario.setCiudad(Integer.parseInt(data[12]));
        formulario.setEmail(data[13]);
        formulario.setBarrio(data[14]);
        formulario.setTelefono_2(data[15]);
        formulario.setCiiu(data[16]);


        return formulario;
    }




    private boolean validateFormulario(Formulario formulario) {
        // Validación para el campo 'codigo'
        if (formulario.getCodigo() != null && formulario.getCodigo().length() > 13) {
            throw new FormularioValidationException(
                    "El campo 'codigo' no puede tenr más de 13 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }



        // Validación para el campo 'sucursal'
        else if (formulario.getSucursal() != null && formulario.getSucursal().length() > 2) {
            throw new FormularioValidationException(
                    "El campo 'sucursal' no puede tener más de 2 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'nitDv'
        else if(formulario.getNit_dv() != null && formulario.getNit_dv().length() > 1) {
            throw new FormularioValidationException(
                    "El campo 'nitDv' no puede tener más de 1 caracter.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }


        // Validación para el campo 'nombre'
        else if (formulario.getNombre() != null && formulario.getNombre().length() > 50) {
            throw new FormularioValidationException(
                    "El campo 'nombre' no puede tener más de 50 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'nombreEstablecimiento'
        else if (formulario.getNombre_establec() != null && formulario.getNombre_establec().length() > 50) {
            throw new FormularioValidationException(
                    "El campo 'nombreEstablecimiento' no puede tener más de 50 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'estado'
        else if(formulario.getEstado() != null && formulario.getEstado().length() > 1) {
            throw new FormularioValidationException(
                    "El campo 'estado' no puede tener más de 1 caracter.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'email'
        else if(formulario.getEmail() != null && formulario.getEmail().length() > 50) {
            throw new FormularioValidationException(
                    "El campo 'email' no puede tener más de 50 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'barrio'
        else if (formulario.getBarrio() != null && formulario.getBarrio().length() > 15) {
            throw new FormularioValidationException(
                    "El campo 'barrio' no puede tener más de 15 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'telefono2'
        else if (formulario.getTelefono_2() != null && formulario.getTelefono_2().length() > 15) {
            throw new FormularioValidationException(
                    "El campo 'telefono2' no puede tener más de 15 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }

        // Validación para el campo 'ciiu'
        else if (formulario.getCiiu() != null && formulario.getCiiu().length() > 6) {
            throw new FormularioValidationException(
                    "El campo 'Ciiu' no puede tener más de 6 caracteres.",
                    400,
                    "Bad Request",
                    "Custom trace information"
            );
        }
        else {
            return true;
        }

    }
    }





