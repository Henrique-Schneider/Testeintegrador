package br.com.teste.Testeintegrador.controller;


import br.com.teste.Testeintegrador.models.Paciente;
import br.com.teste.Testeintegrador.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("")
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.buscarTodos());
        return "pacientes/index";
    }

    @GetMapping("/{id}")
    public String detalhesPaciente(@PathVariable("id") Integer id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", paciente);
        return "pacientes/detalhes";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarPaciente(@ModelAttribute("paciente") Paciente paciente, RedirectAttributes redirectAttributes) {
        pacienteService.cadastrarPaciente(paciente);
        redirectAttributes.addFlashAttribute("success", "Paciente cadastrado com sucesso.");
        return "redirect:/pacientes";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", paciente);
        return "pacientes/editar";
    }

    @PostMapping("/{id}")
    public String editarPaciente(@PathVariable("id") Integer id, @ModelAttribute("paciente") Paciente pacienteAtualizado, RedirectAttributes redirectAttributes) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        paciente.setNome(pacienteAtualizado.getNome());
        paciente.setSobrenome(pacienteAtualizado.getSobrenome());
        paciente.setRg(pacienteAtualizado.getRg());
        paciente.setDataAlta(pacienteAtualizado.getDataAlta());
        paciente.setEndereco(pacienteAtualizado.getEndereco());
        pacienteService.editarPaciente(id, paciente);
        redirectAttributes.addFlashAttribute("success", "Paciente atualizado com sucesso.");
        return "redirect:/pacientes/" + paciente.getId();
    }

    @GetMapping("/{id}/excluir")
    public String excluirPaciente(@PathVariable("id") Integer id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        pacienteService.excluirPaciente(paciente.getId());
        return "redirect:/pacientes";
    }

    // Método para retornar uma lista de pacientes em formato JSON
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Paciente> listarPacientesJson() {
        return pacienteService.buscarTodos();
    }

    // Método para retornar um paciente específico em formato JSON
    @GetMapping(value = "/{id}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Paciente detalhesPacienteJson(@PathVariable("id") Integer id) {
        return pacienteService.buscarPorId(id);
    }

    // Método para cadastrar um paciente a partir de um JSON enviado no corpo da requisição
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String cadastrarPacienteJson(@RequestBody Paciente paciente) {
        pacienteService.cadastrarPaciente(paciente);
        return "Paciente cadastrado com sucesso.";
    }

    // Método para atualizar um paciente a partir de um JSON enviado no corpo da requisição
    @PutMapping(value = "/{id}/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String atualizarPacienteJson(@PathVariable("id") Integer id, @RequestBody Paciente pacienteAtualizado) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "Paciente não encontrado.";
        }
        paciente.setNome(pacienteAtualizado.getNome());
        paciente.setSobrenome(pacienteAtualizado.getSobrenome());
        paciente.setRg(pacienteAtualizado.getRg());
        paciente.setDataAlta(pacienteAtualizado.getDataAlta());
        paciente.setEndereco(pacienteAtualizado.getEndereco());
        pacienteService.editarPaciente(id, paciente);
        return "Paciente atualizado com sucesso.";
    }

    // Método para excluir um paciente a partir de um JSON enviado no corpo da requisição
    @DeleteMapping(value = "/{id}/json")
    @ResponseBody
    public String excluirPacienteJson(@PathVariable("id") Integer id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "Paciente não encontrado.";
        }
        pacienteService.excluirPaciente(paciente.getId());
        return "Paciente excluído com sucesso.";
    }
}
