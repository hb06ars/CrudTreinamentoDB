package org.example.infra.mapper;

import org.example.domain.dto.InscritoDTO;
import org.example.domain.entity.InscritoEntity;
import org.springframework.stereotype.Component;

@Component
public class InscritoMapper {
    public InscritoDTO toDTO(InscritoEntity entity) {
        return InscritoDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .telefone(entity.getTelefone())
                .build();
    }

    public InscritoEntity toEntity(InscritoDTO dto) {
        return InscritoEntity.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .build();
    }
}