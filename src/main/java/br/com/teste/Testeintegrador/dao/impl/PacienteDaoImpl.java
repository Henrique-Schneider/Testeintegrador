package br.com.teste.Testeintegrador.dao.impl;


import br.com.teste.Testeintegrador.config.ConfiguracaoJDBC;
import br.com.teste.Testeintegrador.dao.IDao;
import br.com.teste.Testeintegrador.models.Paciente;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Repository
public class PacienteDaoImpl implements IDao<Paciente> {
    private ConfiguracaoJDBC configuracaoJDBC;

    public PacienteDaoImpl() {
        this.configuracaoJDBC = new ConfiguracaoJDBC();
    }
    @Override
    public List<Paciente> buscarTodos() {
        String sql = "SELECT * FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(resultSet.getInt("id"));
                paciente.setNome(resultSet.getString("nome"));
                paciente.setSobrenome(resultSet.getString("sobrenome"));
                paciente.setRg(resultSet.getString("rg"));
                paciente.setDataAlta(resultSet.getDate("data_alta").toLocalDate());
                paciente.setEndereco(resultSet.getString("endereco"));

                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    @Override
    public Paciente buscarPorId(Integer id)  {
        Paciente paciente = null;
        String query = "SELECT * FROM paciente WHERE id = ?";
        try {
            Connection connection = configuracaoJDBC.conectarComBancoDeDados();
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                paciente = new Paciente();
                paciente.setId(resultSet.getInt("id"));
                paciente.setNome(resultSet.getString("nome"));
                paciente.setSobrenome(resultSet.getString("sobrenome"));
                paciente.setEndereco(resultSet.getString("endereco"));
                paciente.setRg(resultSet.getString("rg"));
                paciente.setDataAlta(resultSet.getDate("data_alta").toLocalDate());
            } else {
                throw new SQLException("Falha ao buscar paciente, nenhum registro encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paciente;
    }

    public class PacienteNaoEncontradoException extends Exception {
        public PacienteNaoEncontradoException() {
            super("Paciente não encontrado");
        }

    }


    @Override
    public Paciente cadastrar(Paciente paciente) {
        String sql = "INSERT INTO paciente (nome, sobrenome, rg, data_alta, endereco) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, paciente.getNome());
            statement.setString(2, paciente.getSobrenome());
            statement.setString(3, paciente.getRg());
            statement.setDate(4, Date.valueOf(paciente.getDataAlta()));
            statement.setString(5, paciente.getEndereco());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao cadastrar paciente, nenhuma linha foi afetada.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao cadastrar paciente, nenhum ID foi retornado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao cadastrar o paciente: " + e.getMessage());
        }

        return paciente;
    }

    @Override
    public Paciente editar(Paciente paciente) {
        String sql = "UPDATE paciente SET nome=?, sobrenome=?, rg=?, data_alta=?, endereco=? WHERE id=?";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, paciente.getNome());
            preparedStatement.setString(2, paciente.getSobrenome());
            preparedStatement.setString(3, paciente.getRg());
            preparedStatement.setDate(4, Date.valueOf(paciente.getDataAlta()));
            preparedStatement.setString(5, paciente.getEndereco());
            preparedStatement.setInt(6, paciente.getId());

            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Não foi possível atualizar o paciente com ID " + paciente.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paciente;
    }

    @Override
    public void excluir(Integer id) {
        try {
            Connection connection = configuracaoJDBC.conectarComBancoDeDados();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM paciente WHERE id = ?");
            preparedStatement.setInt(1, id);
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new PacienteNaoEncontradoException();
            }
        } catch (SQLException | PacienteNaoEncontradoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Paciente cadastrarConsulta(Integer idDentista, Integer idPaciente, LocalDate dataConsulta) {
        return null;
    }


}
