document.getElementById('myForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Evita que el formulario se envíe de la manera tradicional

    // Recopila los datos del formulario
    var formDataObject = {
        codigo: document.getElementById('codigo').value,
        sucursal: document.getElementById('sucursal').value,
        nit: document.getElementById('nit').value,
        nit_dv: document.getElementById('nit_dv').value,
        tipo_terc: document.getElementById('tipo_terc').value,
        ind_rut: document.getElementById('ind_rut').value,
        nombre: document.getElementById('nombre').value,
        nombre_establec: document.getElementById('nombre_establec').value,
        tipo_ident: document.getElementById('tipo_ident').value,
        estado: document.getElementById('estado').value,
        pais: document.getElementById('pais').value,
        dpto: document.getElementById('dpto').value,
        ciudad: document.getElementById('ciudad').value,
        email: document.getElementById('email').value,
        barrio: document.getElementById('barrio').value,
        telefono_2: document.getElementById('telefono_2').value,
        ciiu: document.getElementById('ciiu').value
    };

    // Realiza la validación de campos requeridos
    var requiredFields = ['codigo', 'sucursal', 'nit', 'nit_dv', 'tipo_terc', 'ind_rut', 'nombre', 'nombre_establec', 'tipo_ident', 'estado', 'pais', 'dpto', 'ciudad', 'email', 'barrio', 'telefono_2', 'ciiu'];
    var isValid = true;

    for (var i = 0; i < requiredFields.length; i++) {
        var fieldName = requiredFields[i];
        var fieldValue = formDataObject[fieldName];

        if (!fieldValue) {
            // Si algún campo requerido está vacío, establece isValid en falso y muestra un mensaje de error
            console.error('Campo requerido vacío:', fieldName);
            isValid = false;
            break; // Termina el bucle tan pronto como se encuentra un campo vacío
        }
    }

    if (isValid) {
        // Si todos los campos requeridos están completos, realiza la solicitud Fetch
        fetch('http://localhost:8080/formularios/formulario-create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formDataObject),
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
            limpiarCampos();
            window.location.href="/index.html";
        });
    } else {
        // Puedes mostrar un mensaje al usuario indicando que debe completar todos los campos requeridos
        console.error('Por favor, complete todos los campos requeridos.');
    }
    
});
function limpiarCampos(){
    document.getElementById('codigo').value = "";
    document.getElementById('sucursal').value = "";
    document.getElementById('nit').value = "";
    document.getElementById('nit_dv').value = "";
    document.getElementById('tipo_terc').value = "";
    document.getElementById('ind_rut').value = "";
    document.getElementById('nombre').value = "";
    document.getElementById('nombre_establec').value = "";
    document.getElementById('tipo_ident').value = "";
    document.getElementById('estado').value = "";
    document.getElementById('pais').value = "";
    document.getElementById('dpto').value = "";
    document.getElementById('ciudad').value = "";
    document.getElementById('email').value = "";
    document.getElementById('barrio').value = "";
    document.getElementById('telefono_2').value = "";
    document.getElementById('ciiu').value = ""; 
    
};



