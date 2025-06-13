package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "El NIF debe tener 8 números seguidos de una letra mayúscula")
    @NotBlank(message = "El NIF no puede estar vacío")
    private String nif;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$", message = "El nombre solo puede contener letras y espacios")
    private String name;

    @NotBlank(message = "El primer apellido no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$", message = "El apellido solo puede contener letras y espacios")
    private String surname1;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$", message = "El apellido solo puede contener letras y espacios")
    private String surname2;

    @Past(message = "La fecha de nacimiento debe ser pasada")
    private LocalDateTime birthDate;

    @NotBlank(message = "La nacionalidad no puede estar vacía")
    private String nationality;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe tener 5 dígitos numéricos")
    private String postalCode;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "client_reservation",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "reservation_id")
    )
    @ToString.Exclude
    private Set<Reservation> reservations;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

}