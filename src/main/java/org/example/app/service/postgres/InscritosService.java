package org.example.app.service.postgres;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.PaginatedResponse;
import org.example.domain.entity.InscritoEntity;
import org.example.infra.exceptions.ObjectNotFoundException;
import org.example.infra.repository.postgres.CustomInscritoRepository;
import org.example.infra.repository.postgres.InscritosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class InscritosService {

    @Autowired
    private InscritosRepository repository;

    @Autowired
    private CustomInscritoRepository repositoryCustom;

    public Flux<InscritoEntity> findAll() {
        return repository.findAll();
    }

    public Mono<PaginatedResponse<InscritoEntity>> findAllPeageble(int page, int size, String orderBy, String direction) {
        return repositoryCustom.findAllPeageble(page, size, orderBy, direction)
                .collectList()
                .zipWith(repository.count())
                .map(tuple -> {
                    List<InscritoEntity> content = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);
                    return new PaginatedResponse<>(content, totalPages, totalElements, page);
                });
    }

    public Mono<InscritoEntity> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException("Inscrito não encontrado com id: " + id)));
    }

    public Mono<InscritoEntity> save(InscritoEntity inscritoEntity) {
        return repository.save(inscritoEntity)
                .doOnSuccess(savedEntity -> log.info("Inscrito salvo"))
                .doOnError(error -> log.error("Erro ao salvar inscrito: ", error));
    }

    public Mono<InscritoEntity> update(Long id, InscritoEntity updatedInscritoEntity) {
        return repository.findById(id)
                .flatMap(existingInscrito -> {
                    existingInscrito.setId(id);
                    existingInscrito.setNome(updatedInscritoEntity.getNome());
                    existingInscrito.setTelefone(updatedInscritoEntity.getTelefone());
                    return repository.save(existingInscrito);
                })
                .switchIfEmpty(Mono.error(new ObjectNotFoundException("Inscrito não encontrado com id: " + id)));
    }

    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

}
