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

import com.proyecto.frontend.models.Paciente;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpSession httpSession;

	@Value("${api.clinica.baseURI}")
	private String baseURI;

	@GetMapping("/listar")
	public String listarPacientes(Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		ResponseEntity<List> response = restTemplate.exchange(baseURI + "pacientes/listar", HttpMethod.GET,
				new HttpEntity<>(headers), List.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			List<Paciente> pacientes = response.getBody();
			model.addAttribute("pacientes", pacientes);
			return "paciente-listado";
		} else {
			return "error";
		}
	}

	@GetMapping("/formulario")
	public String mostrarFormularioPaciente(Model model) {
		model.addAttribute("paciente", new Paciente());
		return "paciente-formulario";
	}

	@PostMapping("/agregar")
	public String agregarPaciente(@ModelAttribute Paciente paciente, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		System.out.println("Clave recibida en el controlador: " + paciente.getUsuario().getClave());

		System.out.println("Clave: " + paciente.getUsuario().getClave());
		try {
			ResponseEntity<Void> response = restTemplate.exchange(baseURI + "pacientes", HttpMethod.POST,
					new HttpEntity<>(paciente, headers), Void.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				// Éxito: Redirigir o mostrar un mensaje de éxito
				return "redirect:/pacientes/listar";
			} else {
				// Error: Mostrar un mensaje de error o redirigir a una página de error
				model.addAttribute("error", "Hubo un error al agregar el paciente.");
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
	public String eliminarPaciente(@PathVariable int id, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		try {
			restTemplate.exchange(baseURI + "pacientes/" + id, HttpMethod.DELETE, new HttpEntity<>(headers),
					Void.class);

			// Redirige a la lista de pacientes después de eliminar
			return "redirect:/pacientes/listar";
		} catch (HttpClientErrorException.NotFound e) {
			// Manejo de error: Paciente no encontrado
			model.addAttribute("error", "Paciente no encontrado");
			return "error";
		} catch (Exception e) {
			// Manejo de otros errores
			model.addAttribute("error", "Hubo un error al eliminar el paciente.");
			return "error";
		}
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		try {
			ResponseEntity<Paciente> response = restTemplate.exchange(baseURI + "pacientes/" + id, HttpMethod.GET,
					new HttpEntity<>(headers), Paciente.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				Paciente paciente = response.getBody();
				model.addAttribute("paciente", paciente);
				return "paciente-formulario";
			} else {
				// Manejo de error: No se pudo obtener el paciente por ID
				model.addAttribute("error", "No se pudo obtener el paciente por ID.");
				return "error";
			}
		} catch (HttpClientErrorException.NotFound e) {
			// Manejo de error: Paciente no encontrado
			model.addAttribute("error", "Paciente no encontrado");
			return "error";
		} catch (Exception e) {
			// Manejo de otros errores
			model.addAttribute("error", "Hubo un error al obtener el paciente por ID.");
			return "error";
		}
	}

	@PostMapping("/guardarEdicion")
	public String guardarEdicionPaciente(@ModelAttribute Paciente paciente, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		try {
			restTemplate.exchange(baseURI + "pacientes/" + paciente.getId(), HttpMethod.PUT,
					new HttpEntity<>(paciente, headers), Void.class);

			// Redirige a la lista de pacientes después de editar
			return "redirect:/pacientes/listar";
		} catch (HttpClientErrorException.NotFound e) {
			// Manejo de error: Paciente no encontrado
			model.addAttribute("error", "Paciente no encontrado");
			return "error";
		} catch (Exception e) {
			// Manejo de otros errores
			model.addAttribute("error", "Hubo un error al editar el paciente.");
			return "error";
		}
	}
	
	
}
