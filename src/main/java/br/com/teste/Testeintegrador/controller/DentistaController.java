package br.com.teste.Testeintegrador.controller;


import br.com.teste.Testeintegrador.models.Dentista;
import br.com.teste.Testeintegrador.service.DentistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dentistas")
public class DentistaController {

    @Autowired
    private DentistaService dentistaService;

    @GetMapping("")
    public String listarDentistas(Model model) {
        model.addAttribute("dentistas", dentistaService.buscarTodos());
        return "dentistas/index";
    }

    @GetMapping("/{id}")
    public String detalhesDentista(@PathVariable("id") Integer id, Model model) {
        Dentista dentista = dentistaService.buscarPorId(id);
        System.out.println("mostra o resultado da pesquisa por id");
        System.out.println(dentista.getId());
        if (dentista == null) {
            return "redirect:/dentistas";
        }
        model.addAttribute("dentista", dentista);
        return "dentistas/detalhes";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("dentista", new Dentista());
        return "dentistas/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarDentista(@ModelAttribute("dentista") Dentista dentista, RedirectAttributes redirectAttributes) {
        dentistaService.cadastrarDentista(dentista);
        redirectAttributes.addFlashAttribute("success", "Dentista cadastrado com sucesso.");
        return "redirect:/dentistas";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Dentista dentista = dentistaService.buscarPorId(id);
        if (dentista == null) {
            return "redirect:/dentistas";
        }
        model.addAttribute("dentista", dentista);
        return "dentistas/editar";
    }

    @PostMapping("/{id}")
    public String editarDentista(@PathVariable("id") Integer id, @ModelAttribute("dentista") Dentista dentistaAtualizado, RedirectAttributes redirectAttributes) {
        Dentista dentista = dentistaService.buscarPorId(id);
        if (dentista == null) {
            return "redirect:/pacientes";
        }
        dentista.setNome(dentistaAtualizado.getNome());
        dentista.setSobrenome(dentistaAtualizado.getSobrenome());
        dentista.setMatricula(dentistaAtualizado.getMatricula());

        dentistaService.editarDentista(id, dentista);
        redirectAttributes.addFlashAttribute("success", "Dentista atualizado com sucesso.");
        return "redirect:/dentistas/" + dentista.getId();
    }

    @GetMapping("/{id}/excluir")
    public String excluirPaciente(@PathVariable("id") Integer id) {
        Dentista dentista = dentistaService.buscarPorId(id);
        if (dentista == null) {
            return "redirect:/dentistas";
        }
        dentistaService.excluirDentista(dentista.getId());
        return "redirect:/dentistas";
    }

    // Método para retornar uma lista de pacientes em formato JSON
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Dentista> listarDentistasJson() {
        return dentistaService.buscarTodos();
    }

    // Método para retornar um paciente específico em formato JSON
    @GetMapping(value = "/{id}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Dentista detalhesDentistaJson(@PathVariable("id") Integer id) {
        return dentistaService.buscarPorId(id);
    }

    // Método para cadastrar um paciente a partir de um JSON enviado no corpo da requisição
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String cadastrarPacienteJson(@RequestBody Dentista dentista) {
        dentistaService.cadastrarDentista(dentista);
        return "Dentista cadastrado com sucesso.";
    }

    // Método para atualizar um paciente a partir de um JSON enviado no corpo da requisição
    @PutMapping(value = "/{id}/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String atualizarDentistaJson(@PathVariable("id") Integer id, @RequestBody Dentista dentistaAtualizado) {
        Dentista dentista = dentistaService.buscarPorId(id);
        if (dentista == null) {
            return "Paciente não encontrado.";
        }
        dentista.setNome(dentistaAtualizado.getNome());
        dentista.setSobrenome(dentistaAtualizado.getSobrenome());
        dentista.setMatricula(dentistaAtualizado.getMatricula());

        dentistaService.editarDentista(id, dentista);
        return "Paciente atualizado com sucesso.";
    }

    // Método para excluir um paciente a partir de um JSON enviado no corpo da requisição
    @DeleteMapping(value = "/{id}/json")
    @ResponseBody
    public String excluirDentistaJson(@PathVariable("id") Integer id) {
        Dentista dentista = dentistaService.buscarPorId(id);
        if (dentista == null) {
            return "Dentista não encontrado.";
        }
        dentistaService.excluirDentista(dentista.getId());
        return "Dentista excluído com sucesso.";
    }
}
