package com.proyecto.frontend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.proyecto.frontend.models.Medico;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpSession httpSession;
	
	@Value("${api.clinica.baseURI}")
	private String baseURI;
	
	@GetMapping("/listar")
	public String listarMedicos(Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		ResponseEntity<List> response = restTemplate.exchange(baseURI + "medicos/listar", HttpMethod.GET,
				new HttpEntity<>(headers), List.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			List<Medico> medicos = response.getBody();
			model.addAttribute("medicos", medicos);
			return "medico-listado";
		} else {
			return "error";
		}
	}
	
	@GetMapping("/formulario")
	public String mostrarFormularioMedico(Model model) {
		model.addAttribute("medico", new Medico());
		return "medico-formulario";
	}

	@PostMapping("/agregar")
	public String agregarMedico(@ModelAttribute Medico medico, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		System.out.println("Clave recibida en el controlador: " + medico.getUsuario().getClave());

		System.out.println("Clave: " + medico.getUsuario().getClave());
		try {
			ResponseEntity<Void> response = restTemplate.exchange(baseURI + "medicos", HttpMethod.POST,
					new HttpEntity<>(medico, headers), Void.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return "redirect:/medicos/listar";
			} else {
				model.addAttribute("error", "Hubo un error al agregar el médico.");
				return "error";
			}
		} catch (HttpClientErrorException e) {
			model.addAttribute("error", "Error en la solicitud al servidor.");
			return "error";
		} catch (Exception e) {
			model.addAttribute("error", "Hubo un error inesperado.");
			return "error";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarMedico(@PathVariable int id, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		try {
			restTemplate.exchange(baseURI + "medicos/" + id, HttpMethod.DELETE, new HttpEntity<>(headers),
					Void.class);
			
			return "redirect:/medicos/listar";
		} catch (HttpClientErrorException.NotFound e) {
			model.addAttribute("error", "Médico no encontrado");
			return "error";
		} catch (Exception e) {
			model.addAttribute("error", "Hubo un error al eliminar al médico.");
			return "error";
		}
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		try {
			ResponseEntity<Medico> response = restTemplate.exchange(baseURI + "medicos/" + id, HttpMethod.GET,
					new HttpEntity<>(headers), Medico.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				Medico medico = response.getBody();
				model.addAttribute("medico", medico);
				return "medico-formulario";
			} else {
				model.addAttribute("error", "No se pudo obtener el médico por ID.");
				return "error";
			}
		} catch (HttpClientErrorException.NotFound e) {
			model.addAttribute("error", "Médico no encontrado");
			return "error";
		} catch (Exception e) {
			model.addAttribute("error", "Hubo un error al obtener al médico por ID.");
			return "error";
		}
	}

	@PostMapping("/guardarEdicion")
	public String guardarEdicionMedico(@ModelAttribute Medico medico, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		try {
			restTemplate.exchange(baseURI + "medicos/" + medico.getId(), HttpMethod.PUT,
					new HttpEntity<>(medico, headers), Void.class);

			return "redirect:/medicos/listar";
		} catch (HttpClientErrorException.NotFound e) {
			model.addAttribute("error", "Médico no encontrado");
			return "error";
		} catch (Exception e) {
			model.addAttribute("error", "Hubo un error al editar al médico.");
			return "error";
		}
	}
}
