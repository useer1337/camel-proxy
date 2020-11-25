package ru.hostco.camel.proxy.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "TEMPORARY_TOKEN")
public class TemporaryToken {

    @Id
    @SequenceGenerator(name = "SEQ_TEMPORARY_TOKEN", sequenceName = "SEQ_TEMPORARY_TOKEN", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEMPORARY_TOKEN")
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "PATIENT_ID")
    private String patientId;

    @ManyToMany
    @JoinTable(
            name = "TEMPORARY_TOKEN_MIS_ADDRESS",
            joinColumns = { @JoinColumn(name = "ID_TEMPORARY_TOKEN") },
            inverseJoinColumns = { @JoinColumn(name = "ID_MIS_ADDRESS") }
    )
    private Set<MisAddress> misAddresses = new HashSet<>();

}
