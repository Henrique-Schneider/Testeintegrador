<script>
  document.getElementById("formulario").addEventListener("submit", function(event) {
    event.preventDefault();

    var idDentista = parseInt(document.getElementById("dentista_id").value);
    var idPaciente = parseInt(document.getElementById("paciente_id").value);
    var dataConsulta = document.getElementById("data_consulta").value;

    var consulta = {
      dentista_id: idDentista,
      paciente_id: idPaciente,
      data_consulta: dataConsulta
    };

    fetch("consultas/cadastro", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(consulta)
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error(error));
  });
</script>
