package com.proyecto.frontend.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Historial {
	
	private int id;
	private Paciente pacienteH;
	private Medico medicoH;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String fecha;
	private String diagnostico;
	private String tratamineto;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Paciente getPacienteH() {
		return pacienteH;
	}
	public void setPacienteH(Paciente pacienteH) {
		this.pacienteH = pacienteH;
	}
	public Medico getMedicoH() {
		return medicoH;
	}
	public void setMedicoH(Medico medicoH) {
		this.medicoH = medicoH;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getDiagnostico() {
		return diagnostico;
	}
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	public String getTratamineto() {
		return tratamineto;
	}
	public void setTratamineto(String tratamineto) {
		this.tratamineto = tratamineto;
	}
	
	
}
