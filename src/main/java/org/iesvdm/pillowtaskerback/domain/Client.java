package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    private String nif;

    private String name;
    private String surname1;
    private String surname2;
    private LocalDateTime birthDate;
    private String nationality;
    private String address;
    private String postalCode;
    private String phoneNumber;

    @ManyToMany
    private Set<Reservation> reservations;

}
