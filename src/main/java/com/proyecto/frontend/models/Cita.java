package com.proyecto.frontend.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cita {

	private int cita_id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date cita_fecha;
	private String cita_motivo;
	private Paciente paciente;
	private Medico medico;
	public int getCita_id() {
		return cita_id;
	}
	public void setCita_id(int cita_id) {
		this.cita_id = cita_id;
	}
	public Date getCita_fecha() {
		return cita_fecha;
	}
	public void setCita_fecha(Date cita_fecha) {
		this.cita_fecha = cita_fecha;
	}
	public String getCita_motivo() {
		return cita_motivo;
	}
	public void setCita_motivo(String cita_motivo) {
		this.cita_motivo = cita_motivo;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	
	
}
