# cache-de-conteudo

Exercício — Cache de Conteúdo com HashMap no Spring Boot

## Como rodar

Com o JDK 21 ou superior, abra um terminal na pasta do `pom.xml`. No Windows, execute no PowerShell.

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS, use `sh ./mvnw spring-boot:run`. A API fica disponível em `http://localhost:8080` e usa o H2 em memória.

## Como o cache funciona

O `GET /conteudos/{id}` procura o conteúdo em um `ConcurrentHashMap`. Se encontrar uma entrada válida, devolve o conteúdo do cache (**HIT**). Se a entrada estiver ausente ou expirada, espera 2 segundos para simular a lentidão, busca no H2 e guarda o conteúdo encontrado (**MISS**). O header `X-Cache` mostra o resultado. O log registra HIT ou MISS, o ID e o tempo da busca.

O cache guarda até **5 conteúdos**, com TTL de **120 segundos** contados desde o armazenamento. Quando o cache está cheio, a entrada há mais tempo sem acesso sai para dar lugar à nova (**LRU**). Atualizar ou excluir um conteúdo invalida sua entrada.

`GET /cache/estatisticas` mostra o tamanho, o limite, os hits, os misses, as remoções por limite e a taxa de acertos. `DELETE /cache` limpa as entradas e mantém os contadores.

## Demonstração

### MISS e HIT

![Primeira busca do ID 1 no Postman, com MISS em 2,07 s](docs/prints/print-2-1.png)

![Segunda busca do ID 1 no Postman, com HIT em 6 ms](docs/prints/print-2-2.png)

![Log com os tempos de MISS e HIT do ID 1](docs/prints/print-2-3.png)

### Remoção por LRU

![MISS do ID 2 após a entrada do ID 6](docs/prints/print-3-1.png)

![Log da sequência de buscas que preenche o cache e recarrega o ID 2](docs/prints/print-3-2.png)

![Log com HIT do ID 1 após a entrada do ID 7](docs/prints/print-6-2.png)

### Invalidação após PUT

![PUT do ID 1 com o título atualizado](docs/prints/print-4-2.png)

![Log com HIT antes da atualização e MISS na busca seguinte](docs/prints/print-4-1.png)

### Expiração por TTL

![Log com duas buscas do ID 5 separadas por mais de 120 segundos](docs/prints/print-5-1.png)

### Estatísticas

![Estatísticas durante a demonstração](docs/prints/print-6-1.png)

![Estatísticas finais com cinco itens e cinco remoções por limite](docs/prints/print-6-3.png)
