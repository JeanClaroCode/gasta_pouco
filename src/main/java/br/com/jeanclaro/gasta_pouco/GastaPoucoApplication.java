package br.com.jeanclaro.gasta_pouco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.jeanclaro.gasta_pouco"})
public class GastaPoucoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GastaPoucoApplication.class, args);
	}

}
