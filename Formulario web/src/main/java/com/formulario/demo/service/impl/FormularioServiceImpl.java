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


    /*
         el propósito de este método es crear un nuevo formulario en la base de datos después
         de realizar una validación. La validación se realiza antes de intentar guardar
         el formulario para asegurarse de que cumpla con ciertos requisitos o restricciones.
         La validación puede lanzar una excepción  si se detecta algún problema,
         evitando así la persistencia de datos inválidos en la base de datos.
    */
    public Formulario create(Formulario formulario) {
        validateFormulario(formulario); // Lanza una excepción si la validación falla
        return formularioRepository.save(formulario);
    }

    /*
       el propósito de este método es actualizar  un  formulario  ya existente en la base de datos después
       de realizar una validación. La validación se realiza antes de intentar guardar
       el formulario para asegurarse de que cumpla con ciertos requisitos o restricciones.
       La validación puede lanzar una excepción  si se detecta algún problema,
       evitando así la persistencia de datos inválidos en la base de datos.
  */
    @Override
    public Formulario update(Formulario formulario) {
        Formulario updateFormulario = formularioRepository.save(formulario);
        return updateFormulario;
    }

    /*
      El Proposito de este metdo es eliminar un formulario utilizando un identificador que en
      este caso es el id de cada atributo
      */
    @Override
    public void delete(Long id) {
        formularioRepository.deleteById(id);
    }


    /*
          Este método permite gestionar grandes conjuntos de datos de forma eficiente, ya que solo recupera
          una porción de los resultados en lugar de toda la lista a la vez. Esto es especialmente útil en situaciones
          donde la cantidad total de resultados es grande y se desea mostrar o procesar los datos de forma gradual.
     */
    @Override
    public Page<Formulario> read(Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return formularioRepository.findAll(pageable);
    }

    /*
         Este método permite realizar consultas personalizadas con múltiples criterios de filtro y maneja el
         caso en el que no se encuentran resultados, lanzando una excepción personalizada
         Esto proporciona un control más detallado sobre los casos en los que la consulta no produce resultados
     */
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
    // Método para cargar datos desde un archivo CSV
    public void cargarDesdeCSV(MultipartFile archivo) throws IOException {
       // Utilizar un try-with-resources para garantizar la gestión adecuada de recursos
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            // Arreglo para almacenar cada línea del archivo CSV
            String[] linea;
            // Booleano para controlar si la línea actual es la primera (encabezados)
            boolean primeraLinea = true;
            // Leer cada línea del archivo CSV
            while ((linea = csvReader.readNext()) != null) {
                // Verificar si es la primera línea
                if (primeraLinea) {
                    // Omitir la primera línea que contiene los títulos
                    primeraLinea = false;
                    continue;
                }
                //prime por consola la linea
                    System.out.println(Arrays.toString(linea));

                // Parsear los datos de la línea y crear un objeto Formulario
                Formulario formulario = crearFormularioDesdeLineaCSV(linea);

                // Luego, puedes llamar al método existente para guardar en la base de datos
                create(formulario);
            }
            /*
              Captura una excepción específica de tipo CsvValidationException.
              Esta excepción generalmente se lanza cuando hay problemas durante la validación de un archivo CSV,
              como errores en el formato de las líneas o columnas.
            */

        } catch (CsvValidationException e) {
            // Manejar la excepción de validación de CSV (por ejemplo, imprimir el error)
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Utiliza el repositorio de formularios para recuperar todos los registros
    @Override
    public List<Formulario> getAllFormularios() {
        return formularioRepository.findAll();
    }
    // Utiliza el repositorio de formularios para buscar un registro por su identificador
    @Override
    public Formulario findById(Long id) {
        return formularioRepository.findById(id).get();
    }

    /*
     Este método exporta los formularios almacenados en la base de datos a un archivo Excel.
     @return Objeto DTO que contiene los datos del archivo Excel en formato Base64
     */
    @Override
    public EcxelResponseDTO exportToExcel() {
        // Obtener todos los formularios de la base de datos
        List<Formulario> result = formularioRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            // Crear una hoja en el libro de trabajo
            Sheet sheet = workbook.createSheet("Formularios");

            // Encabezados de las columnas en el archivo Excel
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Codigo", "Sucursal", "Nit", "Nit DV", "Tipo Terc", "Ind RUT",
                    "Nombre", "Nombre Establecimiento", "Tipo Ident", "Estado", "Pais", "Dpto", "Ciudad",
                    "Email", "Barrio", "Telefono 2", "CIIU"};
            /*
             itera sobre cada índice del array headers, que contiene los nombres de las columnas
             que se utilizarán como encabezados en el archivo Excel.
             */
            for (int i = 0; i < headers.length; i++) {
                // Crea una celda en la fila de encabezado en la posición 'i'
                Cell cell = headerRow.createCell(i);

                // Asigna el valor del encabezado desde el array 'headers' a la celda
                cell.setCellValue(headers[i]);
            }

            // Llenar el archivo Excel con datos de los formularios
            for (int rowNum = 1; rowNum <= result.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                Formulario formulario = result.get(rowNum - 1);

                // Llenar cada celda en la fila con los datos del formulario
                /*
                 se utiliza para crear una celda en la fila actual (row) de una hoja de trabajo Excel
                 y asignarle el valor del código obtenido del objeto formulario.
                 */
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
                // Escribe el contenido del libro de trabajo en el flujo de salida (ByteArrayOutputStream)
                workbook.write(bos);
                // Convierte el contenido del flujo de salida a un arreglo de bytes
                byte[] excelBytes = bos.toByteArray();

                // Convertir los bytes a Base64
                return EcxelResponseDTO.builder()
                        // Asignar la representación Base64 de los bytes del archivo Excel al atributo 'b64'
                        .b64(Base64.getEncoder().encodeToString(excelBytes))
                        // Finalizar la construcción del objeto EcxelResponseDTO y devolver una instancia completa
                        .build();
            }
            // Se atrapa y maneja cualquier excepción de E/S (Entrada/Salida) que pueda ocurrir durante la creación del archivo Excel.
        } catch (IOException e) {
            // Manejar cualquier excepción de E/S durante la creación del archivo Excel
             // En este caso, se imprime la traza de la excepción para facilitar la identificación y resolución de problemas.
            e.printStackTrace();
        }
        // En caso de error, devuelve null
        return null;
    }

    // Aquí se  mapea los elementos del array a los campos del objeto Formulario
    private Formulario crearFormularioDesdeLineaCSV(String[] linea) {
        // Imprime la línea de datos del CSV en la consola
        System.out.println("crearFormularioDesdeLineaCSV "+Arrays.toString(linea));
        // Divide la línea del CSV utilizando el carácter ";" como delimitador
        String[] data = Arrays.toString(linea).split(";");
        // Imprime la cantidad de elementos en la línea después de la división
        System.out.println("------ "+data.length);
        // Verifica si la línea tiene al menos 17 elementos (campos del objeto Formulario)
        if (data.length < 17) {
            // Lanza una excepción si la línea no tiene suficientes elementos
            throw new IllegalArgumentException("La línea del CSV no tiene suficientes elementos");
    }


        // Datos
            /*
            Este método toma una fila de información de un archivo CSV
            (un tipo de archivo con datos separados por comas),
            la divide en partes y asigna esas partes a diferentes campos del formulario.
            Después de este proceso, obtendrá un objeto de formulario que contiene la información de esa fila CSV.
            En otras palabras, convierte datos almacenados en formato CSV en objetos
            que las aplicaciones pueden entender y utilizar. Es como convertir información de un formato
            a otro que la aplicación puede procesar fácilmente.
            */
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



        //Metodo para la validacion de cada atributo de la tabla
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





