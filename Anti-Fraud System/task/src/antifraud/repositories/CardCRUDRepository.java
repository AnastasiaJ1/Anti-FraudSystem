package antifraud.repositories;

import antifraud.models.Card;
import antifraud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardCRUDRepository extends JpaRepository<Card, Long> {
    @Override
    Optional<Card> findById(Long aLong);

    Optional<Card> findByNumber(String number);
}