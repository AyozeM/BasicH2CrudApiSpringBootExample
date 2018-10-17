package com.cedei.plexus.empleadoswebservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.cedei.plexus.empleadoswebservice.pojos.Empleado;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(EmpleadoRepository empleadoRepository){
		String[][] data = {
			{"Jose", "garcia", "01/01/1975"},
			{"suso", "perez", "25/04/1985"},
			{"Mamel", "martin", "12/05/1999"}
		};

		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		return (evt) -> Arrays.asList(data).forEach(a ->{
			try {
				empleadoRepository.save(new Empleado(a[0],a[1],df.parse(a[2])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
