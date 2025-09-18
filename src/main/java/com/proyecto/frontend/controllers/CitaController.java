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
import org.springframework.web.client.RestTemplate;

import com.proyecto.frontend.models.Cita;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/citas")
public class CitaController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpSession httpSession;

	@Value("${api.clinica.baseURI}")
	private String baseURI;

	@GetMapping("/listar")
	public String listarCitas(Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		ResponseEntity<List> response = restTemplate.exchange(baseURI + "citas/listar", HttpMethod.GET,
				new HttpEntity<>(headers), List.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			List<Cita> citas = response.getBody();
			model.addAttribute("citas", citas);
			return "cita-listado";
		} else {
			return "error";
		}

	}

	@GetMapping("/formulario")
	public String mostrarFormularioCita(Model model) {
		// Lógica para cargar datos necesarios en el modelo, si es necesario
		model.addAttribute("cita", new Cita());
		return "cita-formulario";
	}

	@PostMapping("/guardar")
	public String guardarCita(@ModelAttribute Cita cita, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		// Supongamos que tienes un endpoint para guardar citas en tu API
		ResponseEntity<Void> response = restTemplate.exchange(baseURI + "citas", HttpMethod.POST,
				new HttpEntity<>(cita, headers), Void.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			// Éxito: Redirigir o mostrar un mensaje de éxito
			return "redirect:/citas/listar";
		} else {
			// Error: Mostrar un mensaje de error o redirigir a una página de error
			model.addAttribute("error", "Hubo un error al guardar la cita.");
			return "error";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarCita(@PathVariable int id, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		// Supongamos que tienes un endpoint para eliminar citas en tu API
		ResponseEntity<Void> response = restTemplate.exchange(baseURI + "citas/" + id, HttpMethod.DELETE,
				new HttpEntity<>(headers), Void.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			// Éxito: Redirigir o mostrar un mensaje de éxito
			return "redirect:/citas/listar";
		} else {
			// Error: Mostrar un mensaje de error o redirigir a una página de error
			model.addAttribute("error", "Hubo un error al eliminar la cita.");
			return "error";
		}
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditarCita(@PathVariable int id, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		// Supongamos que tienes un endpoint para obtener los detalles de una cita por
		// ID en tu API
		ResponseEntity<Cita> response = restTemplate.exchange(baseURI + "citas/" + id, HttpMethod.GET,
				new HttpEntity<>(headers), Cita.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			Cita cita = response.getBody();
			model.addAttribute("cita", cita);
			return "cita-formulario"; // Reutiliza el formulario existente para editar
		} else {
			// Error: Mostrar un mensaje de error o redirigir a una página de error
			model.addAttribute("error", "Hubo un error al cargar los detalles de la cita para editar.");
			return "error";
		}
	}

	@PostMapping("/editar/{id}")
	public String editarCita(@PathVariable int id, @ModelAttribute Cita cita, Model model) {
		String token = (String) httpSession.getAttribute("token");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		// Supongamos que tienes un endpoint para editar citas en tu API
		ResponseEntity<Void> response = restTemplate.exchange(baseURI + "citas/" + id, HttpMethod.PUT,
				new HttpEntity<>(cita, headers), Void.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			// Éxito: Redirigir o mostrar un mensaje de éxito
			return "redirect:/citas/listar";
		} else {
			// Error: Mostrar un mensaje de error o redirigir a una página de error
			model.addAttribute("error", "Hubo un error al editar la cita.");
			return "error";
		}
	}

}
