package br.com.teste.Testeintegrador.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Dentista {

        private Integer id;
        private String nome;
        private String sobrenome;
        private String matricula;

        public Dentista(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Dentista{" +
                    "id=" + id +
                    ", nome='" + nome + '\'' +
                    ", sobrenome='" + sobrenome + '\'' +
                    ", matricula='" + matricula + '\'' +
                    '}';
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dentista dentista = (Dentista) o;
        return Objects.equals(id, dentista.id) &&
                Objects.equals(nome, dentista.nome) &&
                Objects.equals(sobrenome, dentista.sobrenome) &&
                Objects.equals(matricula, dentista.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, sobrenome, matricula);
    }
}
