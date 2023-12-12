package com.ayalait.seguridad.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ayalait.seguridad.modelo.DireccionUsuario;
import com.ayalait.seguridad.modelo.ShoppingUsuarios;
import com.ayalait.seguridad.repositorio.ShoppingUsuariosJpaSpring;
import com.ayalait.seguridad.repositorio.UsuariosDireccionJpaSpring;

@Repository
public class ShoppingUsuariosDaoImpl implements ShoppingUsuariosDao {

	@Autowired
	ShoppingUsuariosJpaSpring daoUsuarios;
	
	@Autowired
	UsuariosDireccionJpaSpring daoDire;
	
	@Override
	public void crearUsuarioNuevo(ShoppingUsuarios usuario) {
			daoUsuarios.save(usuario);
	}

	@Override
	public ShoppingUsuarios recuperarUsuarioPorId(String idUsuario) {
		return daoUsuarios.findById(idUsuario).orElse(null);
	}

	@Override
	public ShoppingUsuarios recuperarUsuario(String mail) {
		
		return daoUsuarios.findByEmail(mail);
	}

	@Override
	public void actualizarUsuario(ShoppingUsuarios Usuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cambiarPassword(String idUsuario, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminarUsuarioPorId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarDireccion(DireccionUsuario dire) {
		daoDire.save(dire);
		
	}

	@Override
	public List<DireccionUsuario> recuperarDreccionUsuarioPorId(String idUsuario) {
		return daoDire.findAllByIdusuario(idUsuario);
	}

	@Override
	public void eliminarDreccionUsuarioPorId(int id) {
		daoDire.deleteById(id);
		
	}

}
