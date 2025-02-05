package com.ayalait.seguridad.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ayalait.seguridad.modelo.DireccionUsuario;




public interface UsuariosDireccionJpaSpring extends JpaRepository<DireccionUsuario, Integer>{
	

	List<DireccionUsuario> findAllByIdusuario(String id);
	
	
	@Query("select sa from DireccionUsuario sa join OrdenPago oc on(oc.iddireccion=sa.idaddress) where oc.order_id= :orderId")
	DireccionUsuario getDireccionCompraId(@Param("orderId") String orderId);
}
