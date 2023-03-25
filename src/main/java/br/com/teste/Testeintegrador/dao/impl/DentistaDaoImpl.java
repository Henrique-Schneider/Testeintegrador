package br.com.teste.Testeintegrador.dao.impl;


import br.com.teste.Testeintegrador.config.ConfiguracaoJDBC;
import br.com.teste.Testeintegrador.dao.IDao;
import br.com.teste.Testeintegrador.models.Dentista;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class DentistaDaoImpl implements IDao<Dentista> {
    private ConfiguracaoJDBC configuracaoJDBC;

    public DentistaDaoImpl() {
        this.configuracaoJDBC = new ConfiguracaoJDBC();
    }

    @Override
    public List<Dentista> buscarTodos() {
        String sql = "SELECT * FROM dentista";
        List<Dentista> dentistas = new ArrayList<>();
        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Dentista dentista = new Dentista();
                dentista.setId(resultSet.getInt("id"));
                dentista.setNome(resultSet.getString("nome"));
                dentista.setSobrenome(resultSet.getString("sobrenome"));
                dentista.setMatricula(resultSet.getString("matricula"));


                dentistas.add(dentista);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dentistas;
    }

    @Override
    public Dentista buscarPorId(Integer id) {
        String sql = "SELECT * FROM dentista WHERE id = ?";
        Dentista dentista = null;
        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    dentista = new Dentista();
                    dentista.setId(resultSet.getInt("id"));
                    dentista.setNome(resultSet.getString("nome"));
                    dentista.setSobrenome(resultSet.getString("sobrenome"));
                    dentista.setMatricula(resultSet.getString("matricula"));

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar paciente por ID: " + e.getMessage());
        }
        return dentista;
    }

    public static class DentistaNaoEncontradoException extends Exception {
        public DentistaNaoEncontradoException() {
            super("Dentista não encontrado");
        }
    }

    @Override
    public Dentista cadastrar(Dentista dentista) {
        String sql = "INSERT INTO dentista (nome, sobrenome, matricula) VALUES (?, ?, ?)";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, dentista.getNome());
            preparedStatement.setString(2, dentista.getSobrenome());
            preparedStatement.setString(3, dentista.getMatricula());
            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao cadastrar paciente, nenhuma linha foi afetada.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dentista.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao cadastrar paciente, nenhum ID foi retornado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao cadastrar o paciente: " + e.getMessage());
        }

        return dentista;
    }

    @Override
    public Dentista editar(Dentista dentista) {
        String sql ="UPDATE dentista SET nome=?, sobrenome=?, matricula=? WHERE id=?";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dentista.getNome());
            preparedStatement.setString(2, dentista.getSobrenome());
            preparedStatement.setString(3, dentista.getMatricula());
            preparedStatement.setInt(4, dentista.getId());

            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Não foi possível atualizar o dentista com ID " + dentista.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dentista;
    }

    @Override
    public void excluir(Integer id) {
        try {
            Connection connection = configuracaoJDBC.conectarComBancoDeDados();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM dentista WHERE id = ?");
            preparedStatement.setInt(1, id);
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new DentistaNaoEncontradoException();
            }
        } catch (SQLException | DentistaNaoEncontradoException e) {
            e.printStackTrace();
        }
    }


}
