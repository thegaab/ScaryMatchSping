package br.com.alura.scarymatch.DTO;

import br.com.alura.scarymatch.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       String poster,
                       Categoria genero,
                       String atores,
                       String sinopse) {
}
