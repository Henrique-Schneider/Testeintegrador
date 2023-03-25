package br.com.teste.Testeintegrador.dao.impl;


import br.com.teste.Testeintegrador.config.ConfiguracaoJDBC;
import br.com.teste.Testeintegrador.dao.IDao;
import br.com.teste.Testeintegrador.models.Consulta;
import br.com.teste.Testeintegrador.models.Dentista;
import br.com.teste.Testeintegrador.models.Paciente;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ConsultaDaoImpl implements IDao<Consulta> {

    private ConfiguracaoJDBC configuracaoJDBC;
    @Autowired
    private PacienteDaoImpl pacienteDao;
    @Autowired
    private DentistaDaoImpl dentistaDao;
    @Autowired
    private EntityManager entityManager;

    public ConsultaDaoImpl() {
        this.configuracaoJDBC = new ConfiguracaoJDBC();
    }

    @Override
    public List<Consulta> buscarTodos() {
        String sql = "SELECT c.id, c.data_consulta, " +
                "p.id as paciente_id, p.nome as paciente_nome, p.sobrenome as paciente_sobrenome, " +
                "p.rg as paciente_rg, p.data_alta as paciente_data_alta, p.endereco as paciente_endereco, " +
                "d.id as dentista_id, d.nome as dentista_nome, d.sobrenome as dentista_sobrenome, " +
                "d.matricula as dentista_matricula " +
                "FROM consulta c " +
                "JOIN paciente p ON c.paciente_id = p.id " +
                "JOIN dentista d ON c.dentista_id = d.id";
        List<Consulta> consultas = new ArrayList<>();

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Paciente paciente = new Paciente(
                        resultSet.getInt("paciente_id"),
                        resultSet.getString("paciente_nome"),
                        resultSet.getString("paciente_sobrenome"),
                        resultSet.getString("paciente_rg"),
                        resultSet.getDate("paciente_data_alta").toLocalDate(),
                        resultSet.getString("paciente_endereco")
                );

                Dentista dentista = new Dentista(
                        resultSet.getInt("dentista_id"),
                        resultSet.getString("dentista_nome"),
                        resultSet.getString("dentista_sobrenome"),
                        resultSet.getString("dentista_matricula")
                );

                Consulta consulta = new Consulta(
                        resultSet.getInt("id"),
                        dentista,
                        paciente,
                        resultSet.getDate("data_consulta").toLocalDate()
                );

                consultas.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultas;
    }

    @Override
    public Consulta buscarPorId(Integer id) {
        String sql = "SELECT * FROM consulta WHERE id = ?";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Paciente paciente = pacienteDao.buscarPorId(resultSet.getInt("paciente_id"));
                    Dentista dentista = dentistaDao.buscarPorId(resultSet.getInt("dentista_id"));

                    Consulta consulta = new Consulta();
                    consulta.setId(resultSet.getInt("id"));
                    consulta.setPaciente(paciente);
                    consulta.setDentista(dentista);
                    consulta.setDataConsulta(resultSet.getDate("data_consulta").toLocalDate());

                    return consulta;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Consulta cadastrar(Consulta consulta) {

        String sql = "INSERT INTO consulta (dentista_id,paciente_id, data_consulta) VALUES (?, ?, ?)";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, consulta.getDentista().getId());
            preparedStatement.setInt(2, consulta.getPaciente().getId());
            preparedStatement.setDate(3, Date.valueOf(consulta.getDataConsulta()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Falha ao cadastrar a consulta.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consulta.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao cadastrar a consulta, não foi possível obter o ID gerado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consulta;
    }


    @Override
    public Consulta editar(Consulta consulta) {

        String sql = "UPDATE dentista SET nome=?, sobrenome=?, matricula=? WHERE id=?";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, consulta.getPaciente().getId());
            preparedStatement.setInt(2, consulta.getDentista().getId());
            preparedStatement.setDate(3, Date.valueOf(consulta.getDataConsulta()));
            preparedStatement.setInt(4, consulta.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("A edição da consulta falhou, nenhum registro foi alterado.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return consulta;
    }


    @Override
    public void excluir(Integer id) {
        String sql = "DELETE FROM consulta WHERE id = ?";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("Consulta não encontrada");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Consulta cadastrarConsulta(Integer idDentista, Integer idPaciente, LocalDate dataConsulta) {
        System.out.println("Cadastrando Consulta");
        String sql = "INSERT INTO consulta (dentista_id,paciente_id, data_consulta) VALUES (?, ?, ?)";

        try (Connection connection = configuracaoJDBC.conectarComBancoDeDados();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idDentista);
            ps.setInt(2, idPaciente);
            ps.setDate(3, java.sql.Date.valueOf(dataConsulta));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            Consulta consulta = new Consulta();
            consulta.setDentistaId(idDentista);
            consulta.setPacienteId(idPaciente);
            consulta.setDataConsulta(dataConsulta);
            System.out.println("Consulta cadastrada" + consulta);
            return consulta;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

