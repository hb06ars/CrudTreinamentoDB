package org.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InscritoDTO {

    private Long id;
    private String nome;
    private String telefone;

    public InscritoDTO(InscritoDTO inscritoDTO) {
        id = inscritoDTO.getId();
        nome = inscritoDTO.getNome();
        telefone = inscritoDTO.getTelefone();
    }

    @Override
    public String toString() {
        return "InscritoDTO{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}