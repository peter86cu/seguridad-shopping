package com.ayalait.seguridad.controller;

 import static com.ayalait.seguridad.utils.Constants.ENCABEZADO;
 
 
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

 
import com.ayalait.seguridad.service.*;

@RestController
public class ShoppingController {
	@Autowired
	ShoppingService service;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping(value = "shopping/usuario/id-usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> recuperarUsuarioPorId(@RequestParam("id") String id, HttpServletRequest request) {
		String token = request.getHeader(ENCABEZADO);
		return service.buscarUsuarioPorId(token, id);
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@DeleteMapping(value = "shopping/usuario/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> eliminarUsuarioPorId(@RequestParam("id") String id, HttpServletRequest request) {
		String token = request.getHeader(ENCABEZADO);
		return service.eliminarUsuarioPorId(id, token);

	}

	@GetMapping(value = "shopping/usuario/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> recuperarUsuarios(@RequestParam("mail") String mail, HttpServletRequest request)
			throws Exception {

		String token = request.getHeader(ENCABEZADO);
		return service.recuperarUsuarioPorUser(token, mail);

	}

	@PostMapping(value = "shopping/usuario/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> guardarUsuario(@RequestBody String datos, HttpServletRequest request)
			throws Exception {
		//String token = request.getHeader(ENCABEZADO);

		return service.crearUsuario(datos);

	}

	@PostMapping(value = "shopping/usuario/password/cambio", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> cambiarPassword(@RequestParam("id") String id, @RequestParam("pass") String pass,
			HttpServletRequest request) throws Exception {
		String token = request.getHeader(ENCABEZADO);

		return service.cambiarPassword(token, id, pass);

	}
	
	@PostMapping(value = "shopping/direccion/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> guardarDireccionUsuario(@RequestBody String datos, HttpServletRequest request)
			throws Exception {
		String token = request.getHeader(ENCABEZADO);

		return service.guardarDireccion(datos,token);

	}
	
	@GetMapping(value = "shopping/direccion/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> obtenerDireccionPorUsuario(@RequestParam("id") String id, HttpServletRequest request)
			throws Exception {
		String token = request.getHeader(ENCABEZADO);

		return service.recuperarDreccionUsuarioPorId(id,token);

	}
	
	
	@GetMapping(value = "shopping/direccion/orden-compra", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> obtenerDireccionPorOrdenCompra(@RequestParam("id") String id, HttpServletRequest request)
			throws Exception {

		return service.recuperarDreccionUsuarioPorOrderID(id);

	}
	
	
	@GetMapping(value = "shopping/direccion/id", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> obtenerDireccionPorID(@RequestParam("id") int id, HttpServletRequest request)
			throws Exception {

		return service.recuperarDreccionID(id);

	}
	
	
	@DeleteMapping(value = "shopping/direccion/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> eliminarDireUsuarioPorId(@RequestParam("id") int id, HttpServletRequest request) {
		String token = request.getHeader(ENCABEZADO);
		return service.eliminarDreccionUsuarioPorId(id, token);

	}
	
	@GetMapping(value = "shopping/departamentos/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> obtenerDireccionPorUsuario(@RequestParam("pais") int id, HttpServletRequest request)
			throws Exception {

		return service.obtenerListadoDpto(id);

	}

}
