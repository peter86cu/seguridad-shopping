package com.ayalait.seguridad.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ayalait.modelo.ConfirmarUsuarioShopping;
import com.ayalait.seguridad.dao.ShoppingUsuariosDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import com.ayalait.seguridad.modelo.*;
import com.ayalait.utils.ErrorState;
import com.ayalait.utils.MessageCodeImpl;

import static com.ayalait.seguridad.utils.Constants.*;
import static com.ayalait.seguridad.utils.Constants.PREFIJO_TOKEN;

@Service
public class UserServiceImpl implements UserService {

	AuthenticationManager authManager;

	ErrorState error = new ErrorState();
	java.util.Date fecha = new Date();

	public UserServiceImpl(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	@Autowired
	ShoppingUsuariosDao daoUsuarios;

	

	

	@Override
	public ResponseEntity<String> recuperarUsuarioPorUser( String user) {
		try {
			
					// creamos el objeto con la información del usuario
					ShoppingUsuarios usuarioresult = daoUsuarios.recuperarUsuario(user);
					if (usuarioresult != null)
						return new ResponseEntity<String>(new Gson().toJson(usuarioresult), HttpStatus.OK);
					else
						return new ResponseEntity<String>("No existe el usuario en la base de datos.", HttpStatus.OK);
				

			
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	@Override
	public ResponseEntity<String> buscarUsuarioPorId( String id) {

		try {
			
				try {
					
					ShoppingUsuarios user = daoUsuarios.recuperarUsuarioPorId(id);
					if (user != null) {
						return new ResponseEntity<String>(new Gson().toJson(user), HttpStatus.OK);
					} else {
						return new ResponseEntity<String>("No se encontro el usuario.", HttpStatus.BAD_REQUEST);
					}

				} catch (Exception e) {
					return new ResponseEntity<String>("Token vencido.", HttpStatus.BAD_REQUEST);
				}
			
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	
	

	@Override
	public ResponseEntity<String> recuperarDreccionUsuarioPorId(String idUsuario) {
		try {
			
				

				// creamos el objeto con la información del usuario
				List<DireccionUsuario> direcciones = daoUsuarios.recuperarDreccionUsuarioPorId(idUsuario);
				//if (!direcciones.isEmpty())
					return new ResponseEntity<String>(new Gson().toJson(direcciones), HttpStatus.OK);
				//else
					//return new ResponseEntity<String>("No existe la dirección en la base de datos.", HttpStatus.OK);

			
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	

	

	@Override
	public ResponseEntity<String> actualizarEstadoConfirmacionRegistroUser(String iduser, int estado) {
		try {

			ShoppingUsuarios usuarioresult = daoUsuarios.recuperarUsuario(iduser);
			if (usuarioresult != null) {
				usuarioresult.setState(1);
				daoUsuarios.actualizarUsuario(usuarioresult);

				return new ResponseEntity<String>(new Gson().toJson(usuarioresult), HttpStatus.OK);
			} else
				return new ResponseEntity<String>("No existe el usuario en la base de datos.", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}



	@Override
	public ResponseEntity<String> obtenerListadoDpto(int pais) {
		try {
			List<DptoPais> lst= daoUsuarios.listaDptoPais(pais);
			if(!lst.isEmpty()) {
				return new ResponseEntity<String>(new Gson().toJson(lst), HttpStatus.OK);

			}
			error.setCode(7000);
			return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

}
