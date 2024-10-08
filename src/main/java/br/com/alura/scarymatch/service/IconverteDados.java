package br.com.alura.scarymatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
