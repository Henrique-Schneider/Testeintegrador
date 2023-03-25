package br.com.teste.Testeintegrador.controller;


import br.com.teste.Testeintegrador.dao.impl.ConsultaDaoImpl;
import br.com.teste.Testeintegrador.models.Consulta;
import br.com.teste.Testeintegrador.models.Dentista;
import br.com.teste.Testeintegrador.models.Paciente;
import br.com.teste.Testeintegrador.service.ConsultaService;
import br.com.teste.Testeintegrador.service.DentistaService;
import br.com.teste.Testeintegrador.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private DentistaService dentistaService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("")
    public String listarConsultas(Model model) {
        model.addAttribute("consultas", consultaService.buscarTodos());
        return "consultas/index";
    }

    @GetMapping("/{id}")
    public String detalhesConsulta(@PathVariable("id") Integer id, Model model) {
        Consulta consulta = consultaService.buscarPorId(id);
        if (consulta == null) {
            return "redirect:/consultas";
        }
        model.addAttribute("consulta", consulta);
        model.addAttribute("paciente", consulta.getPaciente());
        model.addAttribute("dentista", consulta.getDentista());
        model.addAttribute("dataConsulta", consulta.getDataConsulta());
        return "consultas/detalhes";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        List<Dentista> dentistas = consultaService.popularSelectDentistas();
        List<Paciente> pacientes = consultaService.popularSelectPacientes();
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("dentistas", dentistas);
        model.addAttribute("consulta", new Consulta(null, null, null)); // inicializa com valores nulos
        System.out.println("Metodo Get Cadastro");
        System.out.println(pacientes);
        System.out.println(dentistas);
        return "consultas/cadastro";
    }
    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Consulta> cadastrarConsulta(@RequestBody Consulta consulta) {
        System.out.println("Teste Metodo Post");
        Dentista dentista = dentistaService.buscarPorId(consulta.getDentista().getId());
        System.out.println(dentista);
        Paciente paciente = pacienteService.buscarPorId(consulta.getPaciente().getId());
        System.out.println(paciente);

        LocalDate dataConsulta = consulta.getDataConsulta();
        if (dentista == null || paciente == null || dataConsulta == null) {
            return ResponseEntity.badRequest().build();
        }
        Consulta cadastrarConsulta = consultaService.cadastrar(consulta);
        if (cadastrarConsulta == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(cadastrarConsulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConsulta(@PathVariable("id") Integer id) {
        Consulta consulta = consultaService.buscarPorId(id);
        if (consulta == null) {
            System.out.println("objeto nulo");
        }
        consultaService.excluir(consulta.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public Consulta atualizarConsulta(@PathVariable("id") Integer id, @RequestBody Consulta consultaAtualizada) {
        Consulta consulta = consultaService.buscarPorId(id);
        if (consulta == null) {
            System.out.println("objeto nulo");
        }
        consulta.setDataConsulta(consultaAtualizada.getDataConsulta());
        consulta.setPaciente(consultaAtualizada.getPaciente());
        consulta.setDentista(consultaAtualizada.getDentista());
        consultaService.editar(consulta);
        return consulta;
    }


}

//@Controller
//@RequestMapping("/consultas")
//public class ConsultaController {
//    @Autowired
//    private PacienteService pacienteService;
//    @Autowired
//    private DentistaService dentistaService;
//    @Autowired
//    private ConsultaService consultaService;
//    @Autowired
//    private ConsultaDaoImpl consultaDao;
//
//    @GetMapping("")
//    public String listarConsultas(Model model) {
//        model.addAttribute("consultas", consultaService.buscarTodos());
//        return "consultas/index";
//    }
//
//    @GetMapping("/{id}")
//    public String detalhesConsulta(@PathVariable("id") Integer id, Model model) {
//        Consulta consulta = consultaService.buscarPorId(id);
//        if (consulta == null) {
//            return "redirect:/consultas";
//        }
//        model.addAttribute("consulta", consulta);
//        model.addAttribute("paciente", consulta.getPaciente());
//        model.addAttribute("dentista", consulta.getDentista());
//        model.addAttribute("dataConsulta", consulta.getDataConsulta());
//        return "consultas/detalhes";
//    }
//
//    @GetMapping("/cadastro")
//    public String exibirFormularioCadastro(Model model) {
//        List<Paciente> pacientes = consultaService.popularSelectPacientes();
//        List<Dentista> dentistas = consultaService.popularSelectDentistas();
//        model.addAttribute("pacientes", pacientes);
//        model.addAttribute("dentistas", dentistas);
//        model.addAttribute("consulta", new Consulta());
//        System.out.println("Metodo Get Cadastro");
//        System.out.println(pacientes);
//        System.out.println(dentistas);
//        return "consultas/cadastro";
//    }
//
//    @PostMapping("/cadastro")
//    public ResponseEntity<Consulta> cadastrarConsulta(@ModelAttribute Consulta consulta) {
//        Dentista dentista = dentistaService.buscarPorId(consulta.getDentistaId());
//        Paciente paciente = pacienteService.buscarPorId(consulta.getPacienteId());
//        LocalDate dataConsulta = consulta.getDataConsulta();
//        System.out.println("Metodo Post" + paciente+ " "+ dentista+" "+ dataConsulta);
//        if (dentista == null || paciente == null || dataConsulta == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Consulta cadastrarConsulta = consultaService.cadastrarConsulta(dentista.getId(), paciente.getId(), dataConsulta);
//
//        if (cadastrarConsulta == null) {
//            return ResponseEntity.internalServerError().build();
//        }
//
//        return ResponseEntity.ok(cadastrarConsulta);
//    }
//
//
//
//
////    @GetMapping("/{id}/editar")
////    public String exibirFormularioEdicao(@PathVariable("id") Integer id, Model model) {
////        Consulta consulta = consultaService.buscarPorId(id);
////        if (consulta == null) {
////            return "redirect:/consultas";
////        }
////        model.addAttribute("consulta", consulta);
////        return "consultas/editar";
////    }
////
////    @PostMapping("/{id}")
////    public String editarConsulta(@PathVariable("id") Integer id, @ModelAttribute("consulta") Consulta consultaAtualizada, RedirectAttributes redirectAttributes) {
////        Consulta consulta = consultaService.buscarPorId(id);
////        if (consulta == null) {
////            return "redirect:/consultas";
////        }
////        consulta.setDataConsulta(consultaAtualizada.getDataConsulta());
////        consulta.setPaciente(consultaAtualizada.getPaciente());
////        consulta.setDentista(consultaAtualizada.getDentista());
////
////        consultaService.editar(consulta);
////        redirectAttributes.addFlashAttribute("success", "Consulta atualizada com sucesso.");
////        return "redirect:/consultas/" + consulta.getId();
////    }
//
//    @GetMapping("/{id}/excluir")
//    public String excluirConsulta(@PathVariable("id") Integer id) {
//        Consulta consulta = consultaService.buscarPorId(id);
//        if (consulta == null) {
//            return "redirect:/consultas";
//        }
//        consultaService.excluir(consulta.getId());
//        return "redirect:/consultas";
//    }
//
//    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public List<Consulta> listarConsultassJson() {
//        return consultaService.buscarTodos();
//    }
//
//    // Método para retornar um paciente específico em formato JSON
//    @GetMapping(value = "/{id}/json", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public Consulta detalhesConsultaJson(@PathVariable("id") Integer id) {
//        return consultaService.buscarPorId(id);
//    }
//
//    // Método para cadastrar um paciente a partir de um JSON enviado no corpo da requisição
////    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
////    @ResponseBody
////    public String cadastrarConsultaJson(@RequestBody Consulta consulta) {
////        consultaService.cadastrarConsulta(consulta);
////        return "Paciente cadastrado com sucesso.";
////    }
//}
