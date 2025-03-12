package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Restricción de unicidad (Cada usuario solo podrá tener 1 empleado por hotel)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "hotel_id"}))
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String surname1;
    private String surname2;
    private String dni;
    private String password;
    private TipoEmpleadoEnum type;

    @ManyToOne
    @JsonIgnore
    @NotNull // Evita que llegue null desde la API
    @JoinColumn(name = "user_id", nullable = false) // Evita null en la BD
    private User user;

    @ManyToOne
    @JsonIgnore
    @NotNull
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    Set<Incidence> incidences = new HashSet<>();
}
