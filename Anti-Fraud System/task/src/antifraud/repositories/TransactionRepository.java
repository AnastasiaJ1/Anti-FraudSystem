package antifraud.repositories;

import antifraud.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Override
    Optional<Transaction> findById(Long id);


    List<Transaction> findByNumber(String number);
    @Query(value="select count(distinct t.region) from Transaction t where t.number = :card and t.date>= :start_date and t.date <= :end_date")
    Integer findByDateBetweenWithCardRegion(@Param("start_date") Date start_date,@Param("end_date") Date end_date,@Param("card") String card);
    @Query(value="select count(distinct t.ip) from Transaction t where t.number = :card and t.date>= :start_date and t.date <= :end_date ")
    Integer findByDateBetweenWithCardIp(@Param("start_date") Date start_date,@Param("end_date") Date end_date,@Param("card") String card);

    @Query(value="select count(distinct t.ip) from Transaction t where t.number = :card")
    Integer findByDateBetweenWithCardIp1(@Param("card") String card);


}
