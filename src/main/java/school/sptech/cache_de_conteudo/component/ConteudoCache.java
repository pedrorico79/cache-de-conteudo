package school.sptech.cache_de_conteudo.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import school.sptech.cache_de_conteudo.entity.Conteudo;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConteudoCache {

    private final Map<Long, EntradaCache> entradas = new ConcurrentHashMap<>();
    private final int maxItens;
    private final long ttlSegundos;
    private long hits;
    private long misses;
    private long evictions;

    public ConteudoCache(
            @Value("${app.cache.max-itens}") int maxItens,
            @Value("${app.cache.ttl-segundos}") long ttlSegundos) {
        this.maxItens = maxItens;
        this.ttlSegundos = ttlSegundos;
    }

    public synchronized Optional<Conteudo> buscar(Long id) {
        EntradaCache entrada = entradas.get(id);
        if (entrada == null) {
            misses++;
            return Optional.empty();
        }

        Instant agora = Instant.now();
        if (Duration.between(entrada.armazenadoEm, agora).getSeconds() >= ttlSegundos) {
            entradas.remove(id);
            misses++;
            return Optional.empty();
        }

        entrada.ultimoAcesso = agora;
        hits++;
        return Optional.of(entrada.conteudo);
    }

    public synchronized void guardar(Conteudo conteudo) {
        if (maxItens <= 0 || ttlSegundos <= 0) {
            return;
        }

        Long id = conteudo.getId();
        if (entradas.size() >= maxItens && !entradas.containsKey(id)) {
            entradas.entrySet().stream()
                    .min(Comparator.comparing(entrada -> entrada.getValue().ultimoAcesso))
                    .ifPresent(entrada -> {
                        entradas.remove(entrada.getKey());
                        evictions++;
                    });
        }

        entradas.put(id, new EntradaCache(conteudo, Instant.now()));
    }

    public synchronized void invalidar(Long id) {
        entradas.remove(id);
    }

    public synchronized void limpar() {
        entradas.clear();
    }

    public synchronized Estatisticas consultarEstatisticas() {
        return new Estatisticas(entradas.size(), maxItens, hits, misses, evictions);
    }

    public record Estatisticas(int tamanhoAtual, int limite, long hits, long misses, long evictions) {
        public double hitRatio() {
            double total = (double) hits + misses;
            return total == 0 ? 0 : hits / total;
        }
    }

    private static class EntradaCache {
        private final Conteudo conteudo;
        private final Instant armazenadoEm;
        private Instant ultimoAcesso;

        private EntradaCache(Conteudo conteudo, Instant armazenadoEm) {
            this.conteudo = conteudo;
            this.armazenadoEm = armazenadoEm;
            this.ultimoAcesso = armazenadoEm;
        }
    }
}
