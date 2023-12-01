function filtrar(){
    console.log(document.getElementById("nombre").value);
    // Parámetros para enviar al servicio
const parametros = {
   
  };
  if(document.getElementById("codigo").value != ""){
  parametros.codigo=document.getElementById("codigo").value;

  }
  if(document.getElementById("nombre").value != ""){
    parametros.nombre=document.getElementById("nombre").value;
  
    }
    if( document.getElementById("ciiu").value != ""){
        parametros.ciiu= document.getElementById("ciiu").value;
      
        }
    if( document.getElementById("sucursal").value != ""){
            parametros.sucursal= document.getElementById("sucursal").value;
          
     }
     if( document.getElementById("email").value != ""){
        parametros.email= document.getElementById("email").value;
      
 }

  
  // Construir la URL con los parámetros
  const url = new URL('http://localhost:8080/formularios/filtrar');
  url.search = new URLSearchParams(parametros).toString();
  
  // Hacer la solicitud utilizando fetch
  fetch(url)
  .then((response) => response.json())
    .then(data => {
      // Manejar los datos obtenidos
      console.log(data);
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
    });
    })
    .catch(error => {
      // Manejar errores
      console.error('Error en la solicitud:', error);
    });
}