package antifraud.repositories;

import antifraud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCRUDRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameLowerCase(String s);
}
