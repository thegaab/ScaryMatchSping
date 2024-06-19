package br.com.alura.scarymatch;

import br.com.alura.scarymatch.model.DadosSerie;
import br.com.alura.scarymatch.service.ConsumoAPI;
import br.com.alura.scarymatch.service.ConverteDados;
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
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=a8619a59");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
