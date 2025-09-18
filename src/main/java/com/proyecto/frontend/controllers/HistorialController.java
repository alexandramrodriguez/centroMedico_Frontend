package com.proyecto.frontend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.proyecto.frontend.models.Historial;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/historiales")
public class HistorialController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpSession httpSession;
	
	private static final String BASE_URL = "http://localhost:8081/";

	@GetMapping("/listar")
	public String listarHistoriales(Model model) {
		
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		ResponseEntity<List> response = restTemplate.exchange(BASE_URL + "historiales/listar", HttpMethod.GET,
				new HttpEntity<>(headers), List.class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			List<Historial> historiales = response.getBody();
			model.addAttribute("historiales", historiales);
			return "historial-lista";
		} else {
			return "error";
		}
	}
	
	@GetMapping("/formulario")
    public String mostrarFormularioHistorial(Model model) {
        model.addAttribute("historial", new Historial());
        return "historial-formulario";
    }
	
	@PostMapping("/guardar")
    public String guardarHistorial(@ModelAttribute Historial historial, Model model) {
		
		String token = (String) httpSession.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "historiales", HttpMethod.POST, 
        		new HttpEntity<>(historial, headers), Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/historiales/listar";
        } else {
            model.addAttribute("error", "Hubo un error al guardar lel historial.");
            return "error";
        }
    }
	
	@GetMapping("/eliminar/{id}")
    public String eliminarHistorial(@PathVariable int id, Model model) {

		String token = (String) httpSession.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "historiales/" + id, HttpMethod.DELETE
        		, new HttpEntity<>(headers), Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/historiales/listar";
        } else {
            model.addAttribute("error", "Hubo un error al eliminar el historial.");
            return "error";
        }
    }
	
	@GetMapping("/editar/{id}")
    public String mostrarFormularioEditarHistorial(@PathVariable int id, Model model) {

		String token = (String) httpSession.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<Historial> response = restTemplate.exchange(BASE_URL + "historiales/" + id, HttpMethod.GET
        		, new HttpEntity<>(headers), Historial.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Historial historial = response.getBody();
            model.addAttribute("historial", historial);
            return "historial-formulario";
        } else {
            model.addAttribute("error", "Hubo un error al cargar los detalles del historial para editar.");
            return "error";
        }
    }
	
	@PostMapping("/editar/{id}")
    public String editarHistorial(@PathVariable int id, @ModelAttribute Historial historial, Model model) {
		
		String token = (String) httpSession.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<Void> response = restTemplate.exchange( BASE_URL + "historiales/" + id, HttpMethod.PUT
        		, new HttpEntity<>(historial, headers), Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/historiales/listar";
        } else {
            model.addAttribute("error", "Hubo un error al editar el historial.");
            return "error";
        }
    }
	
}
