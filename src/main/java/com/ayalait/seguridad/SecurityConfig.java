package com.ayalait.seguridad;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{	
	
	 @Autowired
	  private DataSource dataSource;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	//definicion roles y usuarios
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		String pw1=new BCryptPasswordEncoder().encode("admin");
		System.out.println("INICIO "+pw1);

		auth.jdbcAuthentication().dataSource(dataSource)
        	.usersByUsernameQuery("select email, password, state"
            	+ " from shopping_usuarios where  email=?")
        	.authoritiesByUsernameQuery("select u.name,roles.descripcion from shopping_usuarios u  join shopping_rol roles  on(roles.id=u.rol) where u.email=?");;
		
	}
	//definicion de politicas de seguridad
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		//solo los miembros del rol admin podran realizar altas
		//y para ver la lista de contactos, tendran que estar autenticados
		.antMatchers("/usuarios/*").authenticated()
		.antMatchers("/buscar/*").authenticated()	
		.antMatchers("/registrar").authenticated()
		.antMatchers("/empleados").authenticated()
		.antMatchers("/inicio/*").authenticated()
		.antMatchers("/cuenta").authenticated()
		.antMatchers("/user").authenticated()
		.antMatchers("/users-profile").authenticated()
		.antMatchers("/empresas-registradas/*").authenticated()
		.antMatchers("/empresa/*").authenticated()
		.antMatchers("/addempresa").authenticated()		
		.antMatchers("/logout").authenticated()
		.antMatchers("/user").authenticated()
		//.antMatchers("/contactos/**").authenticated()
		.and()		
		.addFilter(new JWTAuthorizationFilter(authenticationManager()))
		.exceptionHandling().accessDeniedPage("/pages-error-404");;
	
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

