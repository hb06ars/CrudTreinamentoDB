package org.example.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inscrito")
public class InscritoEntity implements Serializable {

    @Id
    @Column("id")
    private Long id;

    @Column("nome")
    private String nome;

    @Column("telefone")
    private String telefone;
}