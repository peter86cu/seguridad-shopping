package com.ayalait.seguridad.repositorio;



import org.springframework.data.jpa.repository.JpaRepository;

import com.ayalait.seguridad.modelo.ShoppingUsuarios;




public interface ShoppingUsuariosJpaSpring extends JpaRepository<ShoppingUsuarios, String>{
	
	ShoppingUsuarios findByEmail(String mail);

}
