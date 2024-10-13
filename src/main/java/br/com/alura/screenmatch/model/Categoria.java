package br.com.alura.screenmatch.model;

public enum Categoria {
    ACTION("action"),
    ADVENTURE("romance"),
    COMEDY("comedy"),
    CRIME("crime"),
    DRAMA("drama"),
    FANTASY("fantasy"),
    HISTORICAL("historical"),
    HORROR("horror"),
    MYSTERY("mystery"),
    ROMANCE("romance"),
    THRILLER("thriller"),
    WESTERN("western");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria não encontrada!");
    }
}
