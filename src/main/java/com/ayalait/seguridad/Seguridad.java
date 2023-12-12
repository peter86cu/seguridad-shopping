package com.ayalait.seguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
/*@ComponentScan(basePackages = {"com.ayalait.seguridad.controller","com.ayalait.seguridad.dao","com.ayalait.seguridad.service"})
@EntityScan(basePackages = {"com.ayalait.seguridad.modelo"})
@EnableJpaRepositories(basePackages = {"com.ayalait.seguridad.repositorio"})*/
@SpringBootApplication
public class Seguridad {

	public static void main(String[] args) {
		SpringApplication.run(Seguridad.class, args);
	}

}
