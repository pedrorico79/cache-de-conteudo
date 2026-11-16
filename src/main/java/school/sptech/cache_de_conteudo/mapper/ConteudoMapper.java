package school.sptech.cache_de_conteudo.mapper;

import school.sptech.cache_de_conteudo.dto.ConteudoRequestDto;
import school.sptech.cache_de_conteudo.dto.ConteudoResponseDto;
import school.sptech.cache_de_conteudo.entity.Conteudo;

import java.util.List;

public class ConteudoMapper {
    public static Conteudo toEntity(ConteudoRequestDto dto) {
        Conteudo conteudo = new Conteudo();

        conteudo.setTitulo(dto.getTitulo());
        conteudo.setCorpo(dto.getCorpo());
        conteudo.setAutor(dto.getAutor());

        return conteudo;
    }

    public static ConteudoResponseDto toResponse(Conteudo entity) {
        return new ConteudoResponseDto(
                entity.getId(),
                entity.getTitulo(),
                entity.getCorpo(),
                entity.getAutor(),
                entity.getAtualizadoEm()
        );
    }

    public static List<ConteudoResponseDto> toResponse(List<Conteudo> entities) {
        return entities.stream()
                .map(ConteudoMapper::toResponse)
                .toList();
    }
}
