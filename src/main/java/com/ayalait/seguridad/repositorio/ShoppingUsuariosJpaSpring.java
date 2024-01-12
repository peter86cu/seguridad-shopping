package com.ayalait.seguridad.repositorio;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ayalait.seguridad.modelo.ShoppingUsuarios;




public interface ShoppingUsuariosJpaSpring extends JpaRepository<ShoppingUsuarios, String>{
	
	ShoppingUsuarios findByEmail(String mail);
	@Modifying
	@Transactional
	@Query(value="UPDATE shopping_usuarios SET state=3 WHERE id=:id", nativeQuery=true)
	void eliminarCuenta(String id);

}
