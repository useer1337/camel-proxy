package ru.hostco.camel.proxy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hostco.camel.proxy.entities.MisAddress;

public interface MisAddressRepository extends JpaRepository<MisAddress, Long> {

        MisAddress findByAddressName(String name);

}
