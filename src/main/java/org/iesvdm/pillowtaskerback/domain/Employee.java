package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String surname1;
    private String surname2;
    private String password;
    private TipoEmpleadoEnum type;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @ManyToOne
    private Hotel hotel;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    Set<Incidence> incidences = new HashSet<>();
}
