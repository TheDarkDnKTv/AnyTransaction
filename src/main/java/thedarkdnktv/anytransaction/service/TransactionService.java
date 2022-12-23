package thedarkdnktv.anytransaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thedarkdnktv.anytransaction.data.graphql.inputs.SalesRequestInput;
import thedarkdnktv.anytransaction.data.graphql.types.SalesDto;
import thedarkdnktv.anytransaction.data.repository.TransactionRepository;
import thedarkdnktv.anytransaction.utils.Validators;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public List<SalesDto> getSales(Long customerId, SalesRequestInput request) {
        var from = this.validateLocalTimestamp(request.startDateTime());
        var to = this.validateLocalTimestamp(request.endDateTime());
        return transactionRepository.findAllByCustomerIdAndTimestampBetween(customerId, from, to);
    }

    private Date validateLocalTimestamp(String localDateTime) {
        var dateTime = Validators.validateZonedDateTime(localDateTime);
        return Date.from(dateTime.toInstant());
    }
}
