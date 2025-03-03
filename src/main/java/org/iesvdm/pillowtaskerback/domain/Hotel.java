package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
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
    private Usuario usuario;

    @OneToMany(mappedBy = "hotel" , fetch = FetchType.EAGER)
    Set<Empleado> empleados = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
    Set<Incidencia> incidencias = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
    Set<Habitacion> habitaciones = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
    Set<Invitacion> invitaciones = new HashSet<>();

}
