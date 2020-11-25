package ru.hostco.camel.proxy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hostco.camel.proxy.entities.TemporaryToken;

public interface TemporaryTokenRepository extends JpaRepository<TemporaryToken, Long> {

        // List<TemporaryToken> findByToken(String token);
        TemporaryToken findByToken(String token);

}
