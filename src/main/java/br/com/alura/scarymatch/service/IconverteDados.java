package br.com.alura.scarymatch.service;

public interface IconverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
