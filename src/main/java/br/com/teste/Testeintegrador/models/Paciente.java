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

public class Paciente {
    private Integer id;
    private String nome;
    private String sobrenome;
    private String rg;
    private LocalDate dataAlta;
    private String endereco;

    public Paciente(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", rg='" + rg + '\'' +
                ", dataAlta=" + dataAlta +
                ", endereco='" + endereco + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(id, paciente.id) &&
                Objects.equals(nome, paciente.nome) &&
                Objects.equals(sobrenome, paciente.sobrenome) &&
                Objects.equals(rg, paciente.rg) &&
                Objects.equals(dataAlta, paciente.dataAlta) &&
                Objects.equals(endereco, paciente.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, sobrenome, rg, dataAlta, endereco);
    }
}
