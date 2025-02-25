package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Usuario.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombre;
    private String contraseña;

    @Column(nullable = false)
    private String email;

    @OneToMany (mappedBy = "usuario", fetch = FetchType.EAGER)
    Set<Hotel> hotelesPropios = new HashSet<>();

    @OneToMany (mappedBy = "usuario", fetch = FetchType.EAGER)
    Set<Empleado> empleados = new HashSet<>();

}
