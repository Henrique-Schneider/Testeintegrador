    package br.com.teste.Testeintegrador.models;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.util.Objects;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Consulta {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;


        private Paciente paciente;

        private Integer dentistaId;

        private Integer pacienteId;
        private Dentista dentista;
         @Column(name = "data_consulta")
        private LocalDate dataConsulta;

        public Consulta(Integer dentistaId, Integer pacienteId, LocalDate dataConsulta) {
            this.dentistaId = dentistaId;
            this.pacienteId = pacienteId;
            this.dataConsulta = dataConsulta;
        }

        public Consulta(Integer id, Dentista dentista, Paciente paciente, LocalDate dataConsulta) {
            this.id = id;
            this.paciente = paciente;
            this.dentista = dentista;
            this.dataConsulta = dataConsulta;
        }

        @Override
        public String toString() {
            return "Consulta{" +
                    "id=" + id +
                    ", paciente=" + paciente +
                    ", dentista=" + dentista +
                    ", dataConsulta=" + dataConsulta +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Consulta consulta = (Consulta) o;
            return Objects.equals(id, consulta.id) && Objects.equals(paciente, consulta.paciente) && Objects.equals(dentista, consulta.dentista) && Objects.equals(dataConsulta, consulta.dataConsulta);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, paciente, dentista, dataConsulta);
        }
    }
