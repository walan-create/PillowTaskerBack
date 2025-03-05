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
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombre;
    private String Apellido1;
    private String Apellido2;
    private String contrasenia;
    private TipoEmpleadoEnum tipo;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    private Hotel hotel;

    @OneToMany(mappedBy = "empleado", fetch = FetchType.EAGER)
    Set<Incidencia> incidencias = new HashSet<>();
}
