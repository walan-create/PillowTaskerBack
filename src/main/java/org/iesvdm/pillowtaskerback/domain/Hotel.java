package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String postalCode;
    private String address;

    @ManyToOne
    @ToString.Exclude
    private User owner;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY) //Evita que JPA cargue relaciones innecesarias
    @JsonIgnore // 🔴 Evita problemas de serialización
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Incidence> incidences = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invitation> invitations = new HashSet<>();
}

