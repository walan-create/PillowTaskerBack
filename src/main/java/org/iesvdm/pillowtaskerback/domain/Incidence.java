package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Incidence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;
    private String concept;
    private LocalDateTime date;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

    @ManyToOne
    @ToString.Exclude
    private Employee employee;


}
