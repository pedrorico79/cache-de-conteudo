package school.sptech.cache_de_conteudo.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.cache_de_conteudo.dto.ConteudoRequestDto;
import school.sptech.cache_de_conteudo.dto.ConteudoResponseDto;
import school.sptech.cache_de_conteudo.entity.Conteudo;
import school.sptech.cache_de_conteudo.mapper.ConteudoMapper;
import school.sptech.cache_de_conteudo.service.ConsultaCache;
import school.sptech.cache_de_conteudo.service.ConteudoService;

import java.util.List;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @GetMapping
    public ResponseEntity<List<ConteudoResponseDto>> listarConteudo() {
        List<Conteudo> conteudos = conteudoService.listarConteudos();
        return ResponseEntity.status(200).body(ConteudoMapper.toResponse(conteudos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConteudoResponseDto> buscarPorId(@PathVariable Long id, HttpServletResponse resposta) {
        ConsultaCache consulta = new ConsultaCache();
        try {
            Conteudo conteudo = conteudoService.buscarPorId(id, consulta);
            return ResponseEntity.status(200).body(ConteudoMapper.toResponse(conteudo));
        } finally {
            if (consulta.getStatus() != null) {
                resposta.setHeader("X-Cache", consulta.getStatus().name());
            }
        }
    }

    @PostMapping
    public ResponseEntity<ConteudoResponseDto> cadastrar(@RequestBody @Valid ConteudoRequestDto requestDto) {
        Conteudo conteudoParaSalvar = ConteudoMapper.toEntity(requestDto);
        Conteudo conteudoSalvo = conteudoService.cadastrar(conteudoParaSalvar);
        return ResponseEntity.status(201).body(ConteudoMapper.toResponse(conteudoSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConteudoResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid ConteudoRequestDto requestDto) {
        Conteudo conteudoParaSalvar = ConteudoMapper.toEntity(requestDto);
        Conteudo conteudoSalvo = conteudoService.atualizar(id, conteudoParaSalvar);
        return ResponseEntity.status(200).body(ConteudoMapper.toResponse(conteudoSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        conteudoService.deletarPorId(id);
        return ResponseEntity.status(204).build();
    }


}
