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

    private String nombre;
    private String codigoPostal;
    private String direccion;

    @ManyToOne
    @ToString.Exclude
    private Usuario owner;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY) //Evita que JPA cargue relaciones innecesarias
    @JsonIgnore // 🔴 Evita problemas de serialización
    private Set<Empleado> empleados = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Incidencia> incidencias = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Habitacion> habitaciones = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invitacion> invitaciones = new HashSet<>();
}

