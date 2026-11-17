package school.sptech.cache_de_conteudo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import school.sptech.cache_de_conteudo.component.ConteudoCache;
import school.sptech.cache_de_conteudo.entity.Conteudo;
import school.sptech.cache_de_conteudo.exception.EntidadeNaoEncontradaException;
import school.sptech.cache_de_conteudo.repository.ConteudoRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ConteudoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConteudoService.class);

    private final ConteudoRepository conteudoRepository;
    private final ConteudoCache conteudoCache;

    public ConteudoService(ConteudoRepository conteudoRepository, ConteudoCache conteudoCache) {
        this.conteudoRepository = conteudoRepository;
        this.conteudoCache = conteudoCache;
    }

    public List<Conteudo> listarConteudos() {
        return conteudoRepository.findAll();
    }

    public Conteudo buscarPorId(Long id, ConsultaCache consulta) {
        long inicio = System.nanoTime();
        Optional<Conteudo> conteudoEmCache = conteudoCache.buscar(id);
        consulta.setStatus(conteudoEmCache.isPresent() ? ConsultaCache.Status.HIT : ConsultaCache.Status.MISS);

        try {
            if (conteudoEmCache.isPresent()) {
                return conteudoEmCache.get();
            }

            pausar();
            synchronized (conteudoCache) {
                Conteudo conteudo = conteudoRepository.findById(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("O id informado não foi encontrado"));
                conteudoCache.guardar(conteudo);
                return conteudo;
            }
        } finally {
            long tempoEmMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
            LOGGER.info("Cache {} - id={} - {} ms", consulta.getStatus(), id, tempoEmMillis);
        }
    }

    public Conteudo cadastrar(Conteudo conteudo) {
        return conteudoRepository.save(conteudo);
    }

    public Conteudo atualizar(Long id, Conteudo conteudo) {
        synchronized (conteudoCache) {
            if (!conteudoRepository.existsById(id)) {
                throw new EntidadeNaoEncontradaException("O id informado não foi encontrado");
            }

            conteudo.setId(id);
            Conteudo conteudoSalvo = conteudoRepository.save(conteudo);
            conteudoCache.invalidar(id);
            return conteudoSalvo;
        }
    }

    public void deletarPorId(Long id) {
        synchronized (conteudoCache) {
            if (!conteudoRepository.existsById(id)) {
                throw new EntidadeNaoEncontradaException("O id informado não foi encontrado");
            }

            conteudoRepository.deleteById(id);
            conteudoCache.invalidar(id);
        }
    }

    public ConteudoCache.Estatisticas consultarEstatisticasCache() {
        return conteudoCache.consultarEstatisticas();
    }

    public void limparCache() {
        conteudoCache.limpar();
    }

    private void pausar() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Busca de conteúdo interrompida", e);
        }
    }
}
