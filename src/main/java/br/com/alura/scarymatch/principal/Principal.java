package br.com.alura.scarymatch.principal;

import br.com.alura.scarymatch.model.*;
import br.com.alura.scarymatch.repository.SerieRepository;
import br.com.alura.scarymatch.service.ConsumoApi;
import br.com.alura.scarymatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2eb72d33";

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por titulo
                    5 - Buscar séries por ator
                    6 - Top 5 series
                    7 - Buscar séries por gênero
                    8 - Busca personalizada
                    9 - Buscar episódio por trecho
                    10 - Top 5 episodios por série
                    11 - Buscar episódio por data
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriePersonalizado();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosAposData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Digite o nome da série para busca:");

        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()){

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repositorio.save(serieEncontrada);
        }else{
            System.out.println("Série não encontrada!");
        }


    }

    private void listarSeriesBuscadas(){
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()) {
            System.out.println(serieBusca.get());
        }else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator para busca:");
        var nomeAtor = leitura.nextLine();
        System.out.println("Digite a avaliação mínima:");
        var avaliacao = leitura.nextDouble();
        List<Serie> serieBuscada = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        if(!serieBuscada.isEmpty()){
            System.out.println(serieBuscada.size() + " referência(s) encontrada(s):");
            serieBuscada.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
        }else{
            System.out.println("Referencia ao ator não encontrada!");
        }
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Digite o nome da categoria para busca:");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPtBr(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println(seriesPorCategoria.size() + " referência(s) encontrada(s):");
        seriesPorCategoria.forEach(System.out::println);
    }


    private void buscarSeriePersonalizado() {
        System.out.println("Digite a avaliação mínima:");
        var avaliacao = leitura.nextDouble();
        System.out.println("Digite o número máximo de temporadas:");
        var totalTemporadas = leitura.nextInt();

        List<Serie> resultadoPersonalizado = repositorio.seriesPorTemporadaEAvalicao(avaliacao, totalTemporadas);

        System.out.println(resultadoPersonalizado.size() + " referência(s) encontrada(s):");

        resultadoPersonalizado.forEach(System.out::println);
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio para busca:");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);

        episodiosEncontrados.forEach(e -> System.out.printf(
                "Série: %s Temporada: %d Episódio: %d - %s%n", e.getSerie().getTitulo(),
                e.getTemporada(),
                e.getNumeroEpisodio(),
                e.getTitulo()
        ));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf(
                    "Série: %s Temporada: %d Episódio: %d - %s%n ", e.getSerie().getTitulo(),
                    e.getTemporada(),
                    e.getNumeroEpisodio(),
                    e.getTitulo()
            ));
        }
    }


    private void buscarEpisodiosAposData() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite a data de lançamento para busca:");
            var anolancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anolancamento);
            episodiosAno.forEach(System.out::println);
        }
    }
}