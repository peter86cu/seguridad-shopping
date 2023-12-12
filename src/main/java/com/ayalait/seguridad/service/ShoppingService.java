package com.ayalait.seguridad.service;

import org.springframework.http.ResponseEntity;

public interface ShoppingService {
	ResponseEntity<String> crearUsuario(String usuario, String token);

	ResponseEntity<String> buscarUsuario(String user, String pwd);

	ResponseEntity<String> recuperarUsuarioPorUser(String token, String user);

	ResponseEntity<String> buscarUsuarioPorId(String token, String id);

	ResponseEntity<String> eliminarUsuarioPorId(String id, String token);

	ResponseEntity<String> cambiarPassword(String idUsuario, String pass, String token);

	ResponseEntity<String> guardarDireccion(String dire, String token);

	ResponseEntity<String> recuperarDreccionUsuarioPorId(String idUsuario, String token);

	ResponseEntity<String> eliminarDreccionUsuarioPorId(int id, String token);

}
