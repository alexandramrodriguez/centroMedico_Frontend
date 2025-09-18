package com.proyecto.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.proyecto.frontend.models.AuthResponse;
import com.proyecto.frontend.models.LoginRequest;
import com.proyecto.frontend.models.RegisterRequest;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpSession httpSession;
	
	@Value("${api.clinica.baseURI}")
	private String baseURI;
	
	@GetMapping({"/","/login"})
	public String login(Model model) {
		model.addAttribute("usuario", new LoginRequest());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest loginRequest, Model model) {
	    ResponseEntity<AuthResponse> response = restTemplate.postForEntity(baseURI + "auth/login", loginRequest, AuthResponse.class);
	    if (response.getStatusCode().is2xxSuccessful()) {
	        AuthResponse authResponse = response.getBody();
	        httpSession.setAttribute("token", authResponse.getToken());
	        return "redirect:/index";
	    } else {
	        model.addAttribute("error", "Correo o clave inválida");
	        return "login";
	    }
	}
	

	@GetMapping("/pacientes/registro")
	public String registrar(Model model) {
		model.addAttribute("usuario", new RegisterRequest());
		return "paciente-registro";
	}
	
	@PostMapping("/pacientes/registro")
	public String registrar(@ModelAttribute RegisterRequest registerRequest, Model model) {
		ResponseEntity<AuthResponse> response = restTemplate.postForEntity(baseURI + "auth/register", registerRequest, AuthResponse.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			AuthResponse authResponse = response.getBody();
			httpSession.setAttribute("token", authResponse.getToken());
			return "redirect:/index?exito";
		} else {
			model.addAttribute("error", "Datos invalidos");
			return "redirect:/pacientes/registro?error";
		}
	}
	
	@GetMapping("/index")
	public String inicio() {
		return "index";
	}
	
	@GetMapping("/logout")
	public String cerrarSesion() {
	    httpSession.invalidate();
	    return "redirect:/login";
	}


}
