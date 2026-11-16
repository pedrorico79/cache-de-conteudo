package school.sptech.cache_de_conteudo.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.cache_de_conteudo.dto.ConteudoRequestDto;
import school.sptech.cache_de_conteudo.dto.ConteudoResponseDto;
import school.sptech.cache_de_conteudo.entity.Conteudo;
import school.sptech.cache_de_conteudo.mapper.ConteudoMapper;
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
    public ResponseEntity<ConteudoResponseDto> buscarPorId(@PathVariable Integer id) {
        Conteudo conteudo = conteudoService.buscarPorId(id);
        return ResponseEntity.status(200).body(ConteudoMapper.toResponse(conteudo));
    }

    @PostMapping
    public ResponseEntity<ConteudoResponseDto> cadastrar(@RequestBody @Valid ConteudoRequestDto requestDto) {
        Conteudo conteudoParaSalvar = ConteudoMapper.toEntity(requestDto);
        Conteudo conteudoSalvo = conteudoService.cadastrar(conteudoParaSalvar);
        return ResponseEntity.status(201).body(ConteudoMapper.toResponse(conteudoSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConteudoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid ConteudoRequestDto requestDto) {
        Conteudo conteudoParaSalvar = ConteudoMapper.toEntity(requestDto);
        Conteudo conteudoSalvo = conteudoService.atualizar(id, conteudoParaSalvar);
        return ResponseEntity.status(200).body(ConteudoMapper.toResponse(conteudoSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id) {
        conteudoService.deletarPorId(id);
        return ResponseEntity.status(204).build();
    }


}
