package school.sptech.cache_de_conteudo.service;

import org.springframework.stereotype.Service;
import school.sptech.cache_de_conteudo.entity.Conteudo;
import school.sptech.cache_de_conteudo.exception.EntidadeNaoEncontradaException;
import school.sptech.cache_de_conteudo.repository.ConteudoRepository;

import java.util.List;

@Service
public class ConteudoService {
    private final ConteudoRepository conteudoRepository;

    public ConteudoService(ConteudoRepository conteudoRepository) {
        this.conteudoRepository = conteudoRepository;
    }

    public List<Conteudo> listarConteudos() {
        return conteudoRepository.findAll();
    }

    public Conteudo buscarPorId(Integer id) {
        return conteudoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("O id informado não foi encontrado"));
    }

    public Conteudo cadastrar(Conteudo conteudo) {
        return conteudoRepository.save(conteudo);
    }

    public Conteudo atualizar(Integer id, Conteudo conteudo) {
        if (!conteudoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("O id informado não foi encontrado");
        }

        conteudo.setId(id);
        return conteudoRepository.save(conteudo);
    }

    public void deletarPorId(Integer id) {
        if (!conteudoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("O id informado não foi encontrado");
        }

        conteudoRepository.deleteById(id);
    }


}
