package br.com.alura.scarymatch;

import br.com.alura.scarymatch.model.DadosSerie;
import br.com.alura.scarymatch.service.ConsumoApi;
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
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=supernatural&apikey=2eb72d33");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
