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
@Entity(name = "MIS_ADDRESS")
public class MisAddress {

    @Id
    @SequenceGenerator(name = "SEQ_MIS_ADDRESS", sequenceName = "SEQ_MIS_ADDRESS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MIS_ADDRESS")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ADDRESS_NAME")
    private String addressName;

    @ManyToMany(mappedBy = "misAddresses")
    private Set<TemporaryToken> temporaryTokens = new HashSet<>();
}
