package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.EstadoInvitacionEnum;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String mail;

    private EstadoInvitacionEnum state;
    private LocalDateTime shippingDate;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

}
