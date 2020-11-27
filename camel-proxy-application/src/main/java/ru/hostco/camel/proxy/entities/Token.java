package ru.hostco.camel.proxy.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "TOKEN")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TOKEN")
    @SequenceGenerator(name = "SEQ_TOKEN", sequenceName = "SEQ_TOKEN", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "SESSION")
    private String session;

    @OneToMany(mappedBy = "token", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientIdData> patientIdDataList;

}
