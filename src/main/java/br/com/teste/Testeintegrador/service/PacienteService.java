package br.com.teste.Testeintegrador.service;


import br.com.teste.Testeintegrador.dao.impl.DentistaDaoImpl;
import br.com.teste.Testeintegrador.dao.impl.PacienteDaoImpl;
import br.com.teste.Testeintegrador.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PacienteService {
    @Autowired
    private PacienteDaoImpl pacienteDao;
    @Autowired
    private DentistaDaoImpl dentistaDao;


    @Transactional(readOnly = true)
    public List<Paciente> buscarTodos() {
        return pacienteDao.buscarTodos();
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorId(int id) {
        Paciente paciente = pacienteDao.buscarPorId(id);
        if (paciente == null) {
            throw new RuntimeException("Paciente não encontrado");
        }
        return paciente;
    }

    @Transactional
    public void cadastrarPaciente(Paciente paciente) {
        pacienteDao.cadastrar(paciente);
    }

    @Transactional
    public boolean editarPaciente(Integer id, Paciente paciente) {
        Paciente pacienteExistente = buscarPorId(paciente.getId());
        pacienteExistente.setNome(paciente.getNome());
        pacienteExistente.setSobrenome(paciente.getSobrenome());
        pacienteExistente.setRg(paciente.getRg());
        pacienteExistente.setEndereco(paciente.getEndereco());
        pacienteExistente.setDataAlta(paciente.getDataAlta());
        pacienteDao.editar(pacienteExistente);
        return false;
    }

    @Transactional
    public void excluirPaciente(Integer id) {
        Paciente pacienteExistente = buscarPorId(id);
        pacienteDao.excluir(pacienteExistente.getId());
    }


}

