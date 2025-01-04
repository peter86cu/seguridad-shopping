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
public class ShoppingServiceImpl implements ShoppingService {

	AuthenticationManager authManager;

	ErrorState error = new ErrorState();
	java.util.Date fecha = new Date();

	public ShoppingServiceImpl(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	@Autowired
	ShoppingUsuariosDao daoUsuarios;

	@Override
	public ResponseEntity<String> crearUsuario(String usuario) {
		try {
			ErrorState error = new ErrorState();

			ShoppingUsuarios request = new Gson().fromJson(usuario, ShoppingUsuarios.class);
			String pw1 = new BCryptPasswordEncoder().encode(request.getPassword());
			request.setPassword(pw1);
			request.setState(6);
			request.setIdaddress(1);
			if (daoUsuarios.listadoUsuarios().stream()
					.anyMatch(e -> e.getEmail().equalsIgnoreCase(request.getEmail()))) {
				error.setCode(70002);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
			} else if (daoUsuarios.listadoUsuarios().stream()
					.anyMatch(e -> e.getDocument().equalsIgnoreCase(request.getDocument())
							&& e.getDocument_type().equalsIgnoreCase(request.getDocument_type()))) {

				error.setCode(70003);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

			} else {
				daoUsuarios.crearUsuarioNuevo(request);
				ConfirmarUsuarioShopping confirmar = new ConfirmarUsuarioShopping();
				confirmar.setIdusuario(request.getId());
				confirmar.setToken(getTokenConfirmar(request.getId()));
				return new ResponseEntity<String>(new Gson().toJson(confirmar), HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@Override
	public ResponseEntity<String> buscarUsuario(String user, String pwd) {
		try {
			ShoppingUsuarios usuBloqueo = daoUsuarios.recuperarUsuario(user);
			if (usuBloqueo == null) {
				error.setCode(70004);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

			} else {
				if (usuBloqueo.getState() == 1) {
					Authentication autentication = authManager
							.authenticate(new UsernamePasswordAuthenticationToken(user, pwd));
					// si el usuario está autenticado, se genera el token
					if (autentication.isAuthenticated()) {
						return new ResponseEntity<String>(getToken(autentication), HttpStatus.OK);

					} else {
						error.setCode(406);
						return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
					}
				} else {
					error.setCode(usuBloqueo.getState());
					return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

				}

			}

		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@Override
	public ResponseEntity<String> recuperarUsuarioPorUser(String token, String user) {
		try {
			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				Date authorities = claims.getExpiration();
				if (authorities.before(fecha)) {
					error.setCode(7000);
					return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
				} else {
					// creamos el objeto con la información del usuario
					ShoppingUsuarios usuarioresult = daoUsuarios.recuperarUsuario(user);
					if (usuarioresult != null)
						return new ResponseEntity<String>(new Gson().toJson(usuarioresult), HttpStatus.OK);
					else
						return new ResponseEntity<String>("No existe el usuario en la base de datos.", HttpStatus.OK);
				}

			} else {
				error.setCode(1001);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	@Override
	public ResponseEntity<String> buscarUsuarioPorId(String token, String id) {

		try {
			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				try {
					Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
							.getBody();
					String usuario = claims.getSubject();
					List<String> authorities = (List<String>) claims.get("authorities");

					ShoppingUsuarios user = daoUsuarios.recuperarUsuarioPorId(id);
					if (user != null) {
						return new ResponseEntity<String>(new Gson().toJson(user), HttpStatus.OK);
					} else {
						return new ResponseEntity<String>("No se encontro el usuario.", HttpStatus.BAD_REQUEST);
					}

				} catch (Exception e) {
					return new ResponseEntity<String>("Token vencido.", HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<String>("Token no enviado.", HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	@Override
	public ResponseEntity<String> eliminarUsuarioPorId(String id, String token) {
		try {
			if (token != null) {

				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				Date authorities = claims.getExpiration();
				if (authorities.before(fecha)) {
					error.setCode(7000);
					return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
				} else {
			daoUsuarios.eliminarUsuarioPorId(id);
			return new ResponseEntity<String>(com.ayalait.seguridad.utils.Constants.DELETE_USUARIO_OK, HttpStatus.OK);
				}
			}else {
				error.setCode(1001);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
			}
				
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	private String getToken(Authentication autentication) {
		// en el body del token se incluye el usuario
		// y los roles a los que pertenece, además
		// de la fecha de caducidad y los datos de la firma
		String token = Jwts.builder().setIssuedAt(new Date()) // fecha creación
				.setSubject(autentication.getName()) // usuario
				.claim("authorities", autentication.getAuthorities().stream() // roles
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setExpiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) // fecha caducidad
				.signWith(SignatureAlgorithm.HS512, CLAVE)// clave y algoritmo para firma
				.compact(); // generación del token
		return token;
	}

	private String getTokenConfirmar(String idUsuario) {
		// en el body del token se incluye el usuario
		// y los roles a los que pertenece, además
		// de la fecha de caducidad y los datos de la firma
		String token = Jwts.builder().setIssuedAt(new Date()) // fecha creación
				.setSubject(idUsuario) // usuario
				.setExpiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) // fecha caducidad
				.signWith(SignatureAlgorithm.HS512, CLAVE)// clave y algoritmo para firma
				.compact(); // generación del token
		return token;
	}

	@Override
	public ResponseEntity<String> cambiarPassword(String token, String idUsuario, String pass) {

		try {
			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				String usuario = claims.getSubject();
				List<String> authorities = (List<String>) claims.get("authorities");

				String pw1 = new BCryptPasswordEncoder().encode(pass);
				daoUsuarios.cambiarPassword(idUsuario, pw1);

				return new ResponseEntity<String>("Contraseña cambiada correctamente.", HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("Token no enviado.", HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	@Override
	public ResponseEntity<String> guardarDireccion(String dire, String token) {
		try {
			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				String usuario = claims.getSubject();
				List<String> authorities = (List<String>) claims.get("authorities");

				DireccionUsuario request = new Gson().fromJson(dire, DireccionUsuario.class);
				daoUsuarios.guardarDireccion(request);
				return new ResponseEntity<String>(RESULTADO_OK, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Token no enviado.", HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@Override
	public ResponseEntity<String> recuperarDreccionUsuarioPorId(String idUsuario, String token) {
		try {
			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				String usuario = claims.getSubject();
				List<String> authorities = (List<String>) claims.get("authorities");

				// creamos el objeto con la información del usuario
				List<DireccionUsuario> direcciones = daoUsuarios.recuperarDreccionUsuarioPorId(idUsuario);
				//if (!direcciones.isEmpty())
					return new ResponseEntity<String>(new Gson().toJson(direcciones), HttpStatus.OK);
				//else
					//return new ResponseEntity<String>("No existe la dirección en la base de datos.", HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("Token no enviado.", HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	@Override
	public ResponseEntity<String> eliminarDreccionUsuarioPorId(int id, String token) {
		try {

			if (token != null) {

				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				Date authorities = claims.getExpiration();
				if (authorities.after(fecha)) {
					error.setCode(7000);
					return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
				} else {
					daoUsuarios.eliminarDreccionUsuarioPorId(id);
					return new ResponseEntity<String>("Dirección eliminada correctamente.", HttpStatus.OK);
				}

			} else {
				error.setCode(1001);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause().getMessage(), HttpStatus.NOT_ACCEPTABLE);

		}
	}

	@Override
	public ResponseEntity<String> confirmarRegistroUsuario(String token) {
		try {
			java.util.Date fecha = new Date(System.currentTimeMillis() + TIEMPO_VIDA);
			ErrorState error = new ErrorState();

			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				String idusuario = claims.getSubject();
				Date authorities = claims.getExpiration();

				if (authorities.after(fecha)) {
					error.setCode(7000);
					return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
				} else {
					ShoppingUsuarios usuarioresult = daoUsuarios.recuperarUsuarioPorId(idusuario);
					if (usuarioresult != null) {
						usuarioresult.setState(1);
						daoUsuarios.actualizarUsuario(usuarioresult);

						return new ResponseEntity<String>(MessageCodeImpl.getMensajeServiceUsuarios("70001"),
								HttpStatus.OK);
					} else {
						daoUsuarios.eliminarUsuarioPorId(idusuario);
						error.setCode(7000);
						return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

					}

				}

			} else {
				error.setCode(1001);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

			}
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
	public ResponseEntity<String> obtenrUsuarioPorToken(String token) {
		try {
			java.util.Date fecha = new Date(System.currentTimeMillis() + TIEMPO_VIDA);
			ErrorState error = new ErrorState();

			if (token != null) {
				// Se procesa el token y se recupera el usuario y los roles.

				Claims claims = Jwts.parser().setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				String idusuario = claims.getSubject();
				Date authorities = claims.getExpiration();

				if (authorities.after(fecha)) {
					error.setCode(7000);
					return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);
				} else {
					ShoppingUsuarios usuarioresult = daoUsuarios.recuperarUsuarioPorId(idusuario);
					if (usuarioresult != null) {

						return new ResponseEntity<String>(new Gson().toJson(usuarioresult), HttpStatus.OK);
					} else {
						error.setCode(7000);
						return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

					}

				}

			} else {
				error.setCode(1001);
				return new ResponseEntity<String>(new Gson().toJson(error), HttpStatus.BAD_REQUEST);

			}
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
