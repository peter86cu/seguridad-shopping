package com.ayalait.seguridad.controller;

 
 
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

 
import com.ayalait.seguridad.service.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService service;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping(value = "shopping/usuario/id-usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> recuperarUsuarioPorId(@RequestParam("id") String id, HttpServletRequest request) {
		return service.buscarUsuarioPorId( id);
	}

	

	@GetMapping(value = "shopping/usuario/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> recuperarUsuarios(@RequestParam("mail") String mail, HttpServletRequest request)
			throws Exception {

		return service.recuperarUsuarioPorUser( mail);

	}


	
	@GetMapping(value = "shopping/direccion/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> obtenerDireccionPorUsuario(@RequestParam("id") String id, HttpServletRequest request)
			throws Exception {

		return service.recuperarDreccionUsuarioPorId(id);

	}
	
	

	
	@GetMapping(value = "shopping/departamentos/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> obtenerDireccionPorUsuario(@RequestParam("pais") int id, HttpServletRequest request)
			throws Exception {

		return service.obtenerListadoDpto(id);

	}

}
