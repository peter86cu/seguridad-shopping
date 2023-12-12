package com.ayalait.seguridad.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayalait.seguridad.modelo.DireccionUsuario;




public interface UsuariosDireccionJpaSpring extends JpaRepository<DireccionUsuario, Integer>{
	

	List<DireccionUsuario> findAllByIdusuario(String id);
}
