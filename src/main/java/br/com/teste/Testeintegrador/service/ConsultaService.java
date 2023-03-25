package br.com.teste.Testeintegrador.service;



import br.com.teste.Testeintegrador.dao.impl.ConsultaDaoImpl;
import br.com.teste.Testeintegrador.dao.impl.DentistaDaoImpl;
import br.com.teste.Testeintegrador.dao.impl.PacienteDaoImpl;
import br.com.teste.Testeintegrador.models.Consulta;
import br.com.teste.Testeintegrador.models.Dentista;
import br.com.teste.Testeintegrador.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaService {

    private ConsultaDaoImpl consultaDao;

    private PacienteDaoImpl pacienteDao;


    private DentistaDaoImpl dentistaDao;

    @Autowired
    public ConsultaService(ConsultaDaoImpl consultaDao, PacienteDaoImpl pacienteDao, DentistaDaoImpl dentistaDao) {
        this.consultaDao = consultaDao;
        this.pacienteDao = pacienteDao;
        this.dentistaDao = dentistaDao;
    }

    @Transactional(readOnly = true)
    public List<Consulta> buscarTodos() {
        return consultaDao.buscarTodos();
    }

    @Transactional(readOnly = true)
    public Consulta buscarPorId(Integer id) {
        return consultaDao.buscarPorId(id);
    }


    @Transactional
    public Consulta editar(Consulta consulta) {
        return consultaDao.editar(consulta);
    }

    @Transactional
    public void excluir(Integer id) {
        consultaDao.excluir(id);
    }

    @Transactional(readOnly = true)
    public List<Paciente> popularSelectPacientes() {
        List<Paciente> pacientesOptions = new ArrayList<>();

        List<Paciente> pacientes = pacienteDao.buscarTodos();
        for (Paciente paciente : pacientes) {
            pacientesOptions.add(new Paciente(paciente.getId(), paciente.getNome(), paciente.getSobrenome(), paciente.getRg(), paciente.getDataAlta(), paciente.getEndereco()));
        }

        return pacientesOptions;
    }

    @Transactional(readOnly = true)
    public List<Dentista> popularSelectDentistas() {
        List<Dentista> dentistasOptions = new ArrayList<>();

        List<Dentista> dentistas = dentistaDao.buscarTodos();
        for (Dentista dentista : dentistas) {
            dentistasOptions.add(new Dentista(dentista.getId(), dentista.getNome(), dentista.getSobrenome(), dentista.getMatricula()));
        }

        return dentistasOptions;
    }
@Transactional
    public Consulta cadastrar(Consulta consulta) {
        consultaDao.cadastrar(consulta);
        return consulta;
    }
@Transactional
    public Consulta cadastrarConsulta(Integer idDentista, Integer idPaciente, LocalDate dataConsulta){
        Consulta consulta = new Consulta();

        // Recuperar o paciente e o dentista pelo ID
        Paciente paciente = pacienteDao.buscarPorId(idPaciente);
        System.out.println("Paciente com ID " + idPaciente + " não encontrado");

        Dentista dentista = dentistaDao.buscarPorId(idDentista);
        System.out.println("Dentista com ID " + idDentista + " não encontrado");

        // Preencher os dados da consulta

        consulta.setDentistaId(dentista.getId());
        consulta.setPacienteId(paciente.getId());

        consulta.setDataConsulta(dataConsulta);

        // Salvar a consulta no banco de dados
        return consultaDao.cadastrarConsulta(consulta.getDentistaId(),consulta.getPacienteId(),consulta.getDataConsulta());

    }


}


