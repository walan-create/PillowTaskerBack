package org.iesvdm.pillowtaskerback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.EstadoReservaEnum;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombreReservante;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private EstadoReservaEnum estado;
    private boolean salidaAnticipada;

    @ManyToMany(mappedBy = "reservas")
    private Set<Habitacion> habitaciones = new HashSet<>();

    @ManyToMany(mappedBy = "reservas")
    private Set<Cliente> ocupantes;

    @OneToOne
    private Reserva reservaAnterior;


}
