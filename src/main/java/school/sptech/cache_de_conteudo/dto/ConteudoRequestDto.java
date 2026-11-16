package school.sptech.cache_de_conteudo.dto;

import jakarta.validation.constraints.NotBlank;

public class ConteudoRequestDto {
    @NotBlank
    private String titulo;

    @NotBlank
    private String corpo;

    @NotBlank
    private String autor;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
