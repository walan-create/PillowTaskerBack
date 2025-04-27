package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonIgnore
    private Set<Reservation> reservations;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Hotel hotel;

}
