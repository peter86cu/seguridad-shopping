package com.ayalait.seguridad.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ServerController {
	
	@GetMapping(value="/server",produces=MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<String> validadServidorConeccion( HttpServletRequest request) {
		try {
			return new ResponseEntity<String>("Acceso disponible", HttpStatus.OK);

			
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
