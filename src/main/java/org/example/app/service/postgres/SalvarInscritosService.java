package org.example.app.service.postgres;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.entity.InscritoEntity;
import org.example.infra.exceptions.ObjectNotFoundException;
import org.example.infra.repository.postgres.CustomInscritoRepository;
import org.example.infra.repository.postgres.SalvarInscritosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SalvarInscritosService {

        @Autowired
        private SalvarInscritosRepository repository;

        public Flux<InscritoEntity> findAll() {
            return repository.findAll();
        }

        public Mono<InscritoEntity> findById(Long id) {
            return repository.findById(id)
                    .switchIfEmpty(Mono.error(new ObjectNotFoundException("Inscrito não encontrado com id: " + id)));
        }

        public Mono<InscritoEntity> save(InscritoEntity inscritoEntity) {
            return repository.save(inscritoEntity);
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
