package school.sptech.cache_de_conteudo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.cache_de_conteudo.dto.EstatisticasCacheResponseDto;
import school.sptech.cache_de_conteudo.mapper.CacheMapper;
import school.sptech.cache_de_conteudo.service.ConteudoService;

@RestController
@RequestMapping("/cache")
public class CacheController {
    private final ConteudoService conteudoService;

    public CacheController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasCacheResponseDto> consultarEstatisticas() {
        return ResponseEntity.ok(CacheMapper.toResponse(conteudoService.consultarEstatisticasCache()));
    }

    @DeleteMapping
    public ResponseEntity<Void> limpar() {
        conteudoService.limparCache();
        return ResponseEntity.noContent().build();
    }
}
