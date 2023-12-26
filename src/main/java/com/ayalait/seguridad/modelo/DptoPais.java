package com.ayalait.seguridad.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dpto_pais") 
public class DptoPais  implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	private int iddpto;
	private int idpais;
	private String departamento;
	private int estado;
	
	
	
	public int getIddpto() {
		return iddpto;
	}



	public void setIddpto(int iddpto) {
		this.iddpto = iddpto;
	}



	public int getIdpais() {
		return idpais;
	}



	public void setIdpais(int idpais) {
		this.idpais = idpais;
	}



	public String getDepartamento() {
		return departamento;
	}



	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}



	public int getEstado() {
		return estado;
	}



	public void setEstado(int estado) {
		this.estado = estado;
	}



	public DptoPais() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}