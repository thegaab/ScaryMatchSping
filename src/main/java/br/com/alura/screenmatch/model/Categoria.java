package br.com.alura.screenmatch.model;

public enum Categoria {
    ACTION("action", "ação"),
    ADVENTURE("romance", "aventura"),
    COMEDY("comedy", "comédia"),
    CRIME("crime", "crime"),
    DRAMA("drama", "drama"),
    FANTASY("fantasy", "fantasia"),
    HISTORICAL("historical", "histórico"),
    HORROR("horror", "terror"),
    MYSTERY("mystery", "mistério"),
    ROMANCE("romance", "romance"),
    THRILLER("thriller", "suspense"),
    WESTERN("western", "western");

    private String categoriaOmdb;

    private String categoriaPtBr;

    Categoria(String categoriaOmdb, String categoriaPtBr) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPtBr = categoriaPtBr;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria não encontrada!");
    }

    public static Categoria fromPtBr(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaPtBr.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria não encontrada!");
    }
}
