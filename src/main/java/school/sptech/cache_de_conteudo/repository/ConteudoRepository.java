package school.sptech.cache_de_conteudo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.cache_de_conteudo.entity.Conteudo;

public interface ConteudoRepository extends JpaRepository<Conteudo, Integer> {
}
