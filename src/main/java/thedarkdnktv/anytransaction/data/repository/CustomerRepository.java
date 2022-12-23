package thedarkdnktv.anytransaction.data.repository;

import org.springframework.data.repository.CrudRepository;
import thedarkdnktv.anytransaction.data.entity.CustomerEntity;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}
