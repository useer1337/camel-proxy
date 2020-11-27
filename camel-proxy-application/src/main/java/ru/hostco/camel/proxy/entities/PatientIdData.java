package ru.hostco.camel.proxy.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "PATIENT_ID_DATA")
public class PatientIdData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PATIENT_ID_DATA")
    @SequenceGenerator(name = "SEQ_PATIENT_ID_DATA", sequenceName = "SEQ_PATIENT_ID_DATA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "ADDRESS")
    private String address;

    @ManyToOne
    @JoinColumn(name = "ID_TOKEN")
    private Token token;
}
