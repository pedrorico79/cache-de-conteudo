package school.sptech.cache_de_conteudo.dto;

public record EstatisticasCacheResponseDto(
        int tamanhoAtual,
        int limite,
        long hits,
        long misses,
        long evictions,
        double hitRatio
) {
}
