package org.iesvdm.pillowtaskerback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;
import org.iesvdm.pillowtaskerback.enums.InvitationStateEnum;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String mail;

    private InvitationStateEnum state;
    private LocalDateTime shippingDate;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CredentialTypeEnum credentialType;

}
