package com.ayalait.seguridad.dao;

import java.util.List;

import com.ayalait.seguridad.modelo.DireccionUsuario;
import com.ayalait.seguridad.modelo.ShoppingUsuarios;

public interface ShoppingUsuariosDao {

	void crearUsuarioNuevo(ShoppingUsuarios usuario);

	ShoppingUsuarios recuperarUsuarioPorId(String idUsuario);

	ShoppingUsuarios recuperarUsuario(String mail);

	void actualizarUsuario(ShoppingUsuarios Usuario);

	void cambiarPassword(String idUsuario, String password);

	void eliminarUsuarioPorId(String id);
	
	//DIRECCION
	
	void guardarDireccion(DireccionUsuario dire);
	
	List<DireccionUsuario> recuperarDreccionUsuarioPorId(String idUsuario);
	
	void eliminarDreccionUsuarioPorId(int id);

}
