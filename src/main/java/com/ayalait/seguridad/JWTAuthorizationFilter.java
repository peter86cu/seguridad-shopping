package com.ayalait.seguridad;



import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	public static final String CLAVE="1234";
	public static final long TIEMPO_VIDA = 860_500_000; // 1 dia
	public static final String ENCABEZADO = "Authorization";
	public static final String PREFIJO_TOKEN = "Bearer ";
	public static final String PREFIJO_AUTH = "Basic Auth ";
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(ENCABEZADO);
		if (header == null || !header.startsWith(PREFIJO_TOKEN) || !header.startsWith(PREFIJO_AUTH)) {
			chain.doFilter(req, res);
			return;
		}
		//obtenemos los datos del usuario a partir del token
		UsernamePasswordAuthenticationToken authentication;
		try {
			authentication = getAuthentication(req);
			//la información del usuario se almacena en el contexto de seguridad
			//para que pueda ser utilizada por Spring security durante el 
			//proceso de autorización
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;
		         byte[] encodedAuth = Base64.encodeBase64( 
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		      }};
		}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws Exception {
		//el token viene en la cabecera de la petición
		String token = request.getHeader(ENCABEZADO);
		if (token != null) {
			// Se procesa el token y se recupera el usuario y los roles.
			Claims claims=Jwts.parser()
					.setSigningKey(CLAVE)
					.parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
					.getBody();
			String user = claims.getSubject();
			List<String> authorities=(List<String>) claims.get("authorities");
			if (user != null) {
				//creamos el objeto con la información del usuario
				return new UsernamePasswordAuthenticationToken(user, null, authorities.stream()
													.map(SimpleGrantedAuthority::new)
													.collect(Collectors.toList()));
			}
			return null;
		}
		//throw new Exception("Debe enviar un token");
		return null;
	}
	
	
	private String getToken(Authentication autentication) {
		//en el body del token se incluye el usuario 
		//y los roles a los que pertenece, además
		//de la fecha de caducidad y los datos de la firma
		String token = Jwts.builder()
				.setIssuedAt(new Date()) //fecha creación
				.setSubject(autentication.getName()) //usuario
				.claim("authorities",autentication.getAuthorities().stream() //roles
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setExpiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) //fecha caducidad
				.signWith(SignatureAlgorithm.HS512, CLAVE)//clave y algoritmo para firma
				.compact(); //generación del token
		return token;
	}
}
