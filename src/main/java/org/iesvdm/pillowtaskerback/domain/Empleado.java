package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;

@Data
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
    private String contraseña;
    private TipoEmpleadoEnum tipo;

    @ManyToOne
    @ToString.Exclude
    private Usuario usuario;
}
