package thedarkdnktv.anytransaction.domain.repository;

import org.springframework.data.repository.CrudRepository;
import thedarkdnktv.anytransaction.domain.entity.CustomerEntity;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}
