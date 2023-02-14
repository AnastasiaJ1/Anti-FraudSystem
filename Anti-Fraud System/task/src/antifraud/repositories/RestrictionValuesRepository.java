package antifraud.repositories;

import antifraud.models.Card;
import antifraud.models.RestrictionValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionValuesRepository extends JpaRepository<RestrictionValues, String> {
}
