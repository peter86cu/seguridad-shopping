package com.ayalait.seguridad.service;

import org.springframework.http.ResponseEntity;


public interface UserService {


	ResponseEntity<String> recuperarUsuarioPorUser( String user);

	ResponseEntity<String> buscarUsuarioPorId( String id);

	ResponseEntity<String> recuperarDreccionUsuarioPorId(String idUsuario);
	
	ResponseEntity<String> actualizarEstadoConfirmacionRegistroUser( String iduser, int estado);
	
	ResponseEntity<String> obtenerListadoDpto(int pais);

}
