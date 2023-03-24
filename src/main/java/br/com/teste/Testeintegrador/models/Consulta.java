    package br.com.teste.Testeintegrador.models;

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
        private Integer id;
        private Paciente paciente;
        private Dentista dentista;

        private Integer dentistaId;
        private Integer pacienteId;

        private LocalDate dataConsulta;

        public Consulta(Integer dentistaId, Integer pacienteId, LocalDate dataConsulta) {
            this.dentistaId = dentistaId;
            this.pacienteId = pacienteId;
            this.dataConsulta = dataConsulta;
        }

        public Consulta(Integer id, Paciente paciente, Dentista dentista, LocalDate dataConsulta) {
            this.id = id;
            this.paciente = paciente;
            this.dentista = dentista;
            this.dataConsulta = dataConsulta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Consulta consulta = (Consulta) o;
            return Objects.equals(id, consulta.id) && Objects.equals(paciente, consulta.paciente) && Objects.equals(dentista, consulta.dentista) && Objects.equals(pacienteId, consulta.pacienteId) && Objects.equals(dentistaId, consulta.dentistaId) && Objects.equals(dataConsulta, consulta.dataConsulta);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, paciente, dentista, pacienteId, dentistaId, dataConsulta);
        }

        @Override
        public String toString() {
            return "Consulta{" +
                    "id=" + id +
                    ", paciente=" + paciente +
                    ", dentista=" + dentista +
                    ", pacienteId=" + pacienteId +
                    ", dentistaId=" + dentistaId +
                    ", dataConsulta=" + dataConsulta +
                    '}';
        }
    }
