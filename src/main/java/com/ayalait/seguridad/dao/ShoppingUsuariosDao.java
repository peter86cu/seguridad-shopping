package com.ayalait.seguridad.dao;

import java.util.List;

import com.ayalait.seguridad.modelo.DireccionUsuario;
import com.ayalait.seguridad.modelo.DptoPais;
import com.ayalait.seguridad.modelo.ShoppingUsuarios;

public interface ShoppingUsuariosDao {

	void crearUsuarioNuevo(ShoppingUsuarios usuario);

	ShoppingUsuarios recuperarUsuarioPorId(String idUsuario);

	ShoppingUsuarios recuperarUsuario(String mail);

	void actualizarUsuario(ShoppingUsuarios Usuario);

	void cambiarPassword(String idUsuario, String password);

	void eliminarUsuarioPorId(String id);
	
	List<ShoppingUsuarios> listadoUsuarios();
	
	//DIRECCION
	
	void guardarDireccion(DireccionUsuario dire);
	
	List<DireccionUsuario> recuperarDreccionUsuarioPorId(String idUsuario);
	
	DireccionUsuario recuperarDireccionUsuarioCompra(String orderID);
	
	DireccionUsuario recuperarDireccionID(int id);
	
	void eliminarDreccionUsuarioPorId(int id);
	
	List<DptoPais> listaDptoPais(int pais);

}
