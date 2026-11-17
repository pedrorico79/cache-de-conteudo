package school.sptech.cache_de_conteudo.mapper;

import school.sptech.cache_de_conteudo.component.ConteudoCache;
import school.sptech.cache_de_conteudo.dto.EstatisticasCacheResponseDto;

public class CacheMapper {
    public static EstatisticasCacheResponseDto toResponse(ConteudoCache.Estatisticas estatisticas) {
        return new EstatisticasCacheResponseDto(
                estatisticas.tamanhoAtual(),
                estatisticas.limite(),
                estatisticas.hits(),
                estatisticas.misses(),
                estatisticas.evictions(),
                estatisticas.hitRatio()
        );
    }
}
