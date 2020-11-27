package ru.hostco.camel.proxy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hostco.camel.proxy.entities.PatientIdData;

@Repository
public interface PatientIdDataRepository extends JpaRepository<PatientIdData, Long> {
}
