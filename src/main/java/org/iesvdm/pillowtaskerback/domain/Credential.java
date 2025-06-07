package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Restricción de unicidad (Cada usuario solo podrá tener 1 credencial por hotel)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "hotel_id"}))
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private CredentialTypeEnum rol;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Column(nullable = false)
    private String password;

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



}
