package br.com.teste.Testeintegrador.service;


import br.com.teste.Testeintegrador.dao.impl.DentistaDaoImpl;
import br.com.teste.Testeintegrador.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DentistaService {
    @Autowired
    private DentistaDaoImpl dentistaDao;

    @Transactional(readOnly = true)
    public List<Dentista> buscarTodos() {
        return dentistaDao.buscarTodos();
    }

    @Transactional(readOnly = true)
    public Dentista buscarPorId(int id) {
        Dentista dentista = dentistaDao.buscarPorId(id);
        if (dentista == null) {
            throw new RuntimeException("Dentista não encontrado");
        }
        return dentista;
    }

    @Transactional
    public void cadastrarDentista(Dentista dentista) {
        dentistaDao.cadastrar(dentista);
    }

    @Transactional
    public boolean editarDentista(Integer id, Dentista dentista) {
        Dentista dentistaExistente = buscarPorId(dentista.getId());
        dentistaExistente.setNome(dentistaExistente.getNome());
        dentistaExistente.setSobrenome(dentistaExistente.getSobrenome());
        dentistaExistente.setMatricula(dentista.getMatricula());

        dentistaDao.editar(dentistaExistente);
        return false;
    }
    @Transactional
    public void excluirDentista(Integer id) {
        Dentista dentistaExistente = buscarPorId(id);
        dentistaDao.excluir(dentistaExistente.getId());
    }
}
