package antifraud.repositories;

import antifraud.models.Card;
import antifraud.models.SuspiciousIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SuspiciousIpCRUDRepository extends JpaRepository<SuspiciousIp, Long> {
    @Override
    Optional<SuspiciousIp> findById(Long aLong);

    Optional<SuspiciousIp> findByIp(String ip);
}
