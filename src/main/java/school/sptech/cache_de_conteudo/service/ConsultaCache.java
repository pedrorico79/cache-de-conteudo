package school.sptech.cache_de_conteudo.service;

public class ConsultaCache {
    public enum Status {
        HIT,
        MISS
    }

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
