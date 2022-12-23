package thedarkdnktv.anytransaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thedarkdnktv.anytransaction.domain.entity.TransactionEntity;
import thedarkdnktv.anytransaction.domain.graphql.types.SalesDto;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(
        """
        SELECT
          new thedarkdnktv.anytransaction.domain.graphql.types.SalesDto(
              date_trunc(hour, t.timestamp),
              cast(sum(t.paymentAmount) as string),
              sum(t.points)
          )
        FROM TransactionEntity t
        WHERE t.customer.id = :customerId
        AND t.timestamp between :from and :to
        GROUP BY date_trunc(hour, t.timestamp)
        """
    )
    List<SalesDto> findAllByCustomerIdAndTimestampBetween(Long customerId, Date from, Date to);
}
