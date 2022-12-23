package thedarkdnktv.anytransaction.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thedarkdnktv.anytransaction.data.entity.TransactionEntity;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllByTimestampBetween(Date timestampFrom, Date timestampTo);
}
