package br.com.teste.Testeintegrador.dao;

import java.time.LocalDate;
import java.util.List;

public interface IDao<T> {
    List<T> buscarTodos();
    T buscarPorId(Integer id);
    T cadastrar(T objeto);
    T editar(T objeto);
    void excluir(Integer id);
    T cadastrarConsulta(Integer idDentista, Integer idPaciente, LocalDate dataConsulta);
}
