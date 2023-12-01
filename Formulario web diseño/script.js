document.addEventListener("DOMContentLoaded", function () {
  // Llamada a la API
  fetch("http://localhost:8080/formularios/todos")
    .then((response) => response.json())
    .then((data) => {
      // Llenar la tabla con los datos obtenidos de la API
      const tbody = document.querySelector("tbody");

      data.forEach((formulario, index) => {
        const row = tbody.insertRow();
        const cellIndex = row.insertCell(0);
        const cellCodigo = row.insertCell(1);
        const cellSucursal = row.insertCell(2);
        const cellNit = row.insertCell(3);
        const cellNit_dv = row.insertCell(4);
        const cellTipoTerc = row.insertCell(5);
        const cellIndRut = row.insertCell(6);
        const cellNombre = row.insertCell(7);
        const cellNombreEstablec = row.insertCell(8);
        const cellTipoIdent = row.insertCell(9);
        const cellEstado = row.insertCell(10);
        const cellPais = row.insertCell(11);
        const cellDpto = row.insertCell(12);
        const cellCiudad = row.insertCell(13);
        const cellEmail = row.insertCell(14);
        const cellBarrio = row.insertCell(15);
        const cellTelefono2 = row.insertCell(16);
        const cellCiiu = row.insertCell(17);
        const cellEdit = row.insertCell(18);
        const cellDelete = row.insertCell(19);

        // <!-- Reemplazar el contenido de la celda con el ícono -->
        cellEdit.innerHTML = `<i class="fa-solid fa-pen-to-square edit-icon" style="color: #000000;" data-id="${formulario.id}" ></i>`;
        cellDelete.innerHTML = `<i class="fa-solid fa-trash delete-icon" style="color: red;" data-id="${formulario.id}" onClick="deleteForm(${formulario.id})" ></i>`;

        // Llenar las celdas con los datos del formulario
        cellIndex.textContent = index + 1;
        cellCodigo.textContent = formulario.codigo;
        cellSucursal.textContent = formulario.sucursal;
        cellNit.textContent = formulario.nit;
        cellNit_dv.textContent = formulario.nit_dv;
        cellTipoTerc.textContent = formulario.tipo_terc;
        cellIndRut.textContent = formulario.ind_rut;
        cellNombre.textContent = formulario.nombre;
        cellNombreEstablec.textContent = formulario.nombre_establec;
        cellTipoIdent.textContent = formulario.tipo_ident;
        cellEstado.textContent = formulario.estado;
        cellPais.textContent = formulario.pais;
        cellDpto.textContent = formulario.dpto;
        cellCiudad.textContent = formulario.ciudad;
        cellEmail.textContent = formulario.email;
        cellBarrio.textContent = formulario.barrio;
        cellTelefono2.textContent = formulario.telefono_2;
        cellCiiu.textContent = formulario.ciiu;

        // Llenar las celdas restantes aquí con l   // Agregar manejador de eventos a los íconos de edición
        const editIcons = document.querySelectorAll(".edit-icon");
        editIcons.forEach((icon) => {
          icon.addEventListener("click", function () {
            const formularioId = this.getAttribute("data-id");
            // Guardar el formularioId en el localStorage
            localStorage.setItem("formularioId", formularioId);
            // Redirigir a la página de edición
            window.location.href = "editar.html";
          });
        });
        
      });
    })
});

document.getElementById("fileInput").addEventListener("change", function () {
  // Acciones que deseas realizar cuando se selecciona un archivo
  const selectedFile = this.files[0];
  console.log("Archivo seleccionado:", selectedFile);

  // Crear un FormData y agregar el archivo
  const formData = new FormData();
  formData.append("archivo", selectedFile);

  // Realizar el fetch
  fetch("http://localhost:8080/formularios/cargar-csv", {
    method: "POST",
    body: formData,
  })
    .then((response) => response.json())
    .then(() => {
      // Manejar la respuesta del servidor si es necesario
      window.location.reload();
    })
    .catch(() => {
      window.location.reload();
    });
});
document.getElementById("fileUploadInput").addEventListener("click", function () {
    // Acciones que deseas realizar cuando se selecciona un archivo
  
  
  
    // Realizar el fetch
    fetch("http://localhost:8080/formularios/export-to-excel", {
      method: "GET"
    })
      .then((response) => response.json())
      .then(data => {
        // Manejar la respuesta del servidor si es necesario
        console.log("data --------> "+data);
        // Supongamos que tu JSON se almacena en la variable jsonData
var jsonData = data;
  
  // Obtén la cadena base64 del JSON
  var base64Data = jsonData.b64;
  
  // Decodifica la cadena base64 a un array de bytes
  var byteCharacters = atob(base64Data);
  
  // Convierte el array de bytes en un objeto Uint8Array
  var byteNumbers = new Array(byteCharacters.length);
  for (var i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  var byteArray = new Uint8Array(byteNumbers);
  
  // Crea un objeto Blob con el array de bytes y el tipo MIME
  var blob = new Blob([byteArray], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
  
  // Crea un enlace para descargar el archivo
  var enlaceDescarga = document.createElement('a');
  enlaceDescarga.href = window.URL.createObjectURL(blob);
  enlaceDescarga.download = 'archivo_excel.xlsx';
  
  // Añade el enlace al cuerpo del documento
  document.body.appendChild(enlaceDescarga);
  
  // Simula un clic en el enlace para iniciar la descarga
  enlaceDescarga.click();
  
  // Elimina el enlace del cuerpo del documento
  document.body.removeChild(enlaceDescarga);
      })
      .catch(() => {
        
      });
  });
function deleteForm(id){
    if(confirm("¿Quieres eliminar el registro?")){
        fetch(`http://localhost:8080/formularios/formulario-delete/${id}`, {
method: 'DELETE',
headers: {
    'Content-Type': 'application/json',
}
})
.then(response => response.json())
.then(data => {
// Manejar la respuesta del servidor si es necesario
console.log('Respuesta del servidor:', data);
})
.catch(error => {
console.error('Error al enviar datos:', error);
})
.finally(()=>{
window.location.reload();
});
    }
    };

