package br.com.alura.scarymatch;

import br.com.alura.scarymatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScarymatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScarymatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
