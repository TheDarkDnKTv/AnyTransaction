package thedarkdnktv.anytransaction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import thedarkdnktv.anytransaction.data.graphql.inputs.SalesRequestInput;
import thedarkdnktv.anytransaction.data.graphql.inputs.TransactionInput;
import thedarkdnktv.anytransaction.data.graphql.types.SalesDto;
import thedarkdnktv.anytransaction.data.graphql.types.TransactionResult;
import thedarkdnktv.anytransaction.service.TransactionService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping("sales")
    public List<SalesDto> getSales(@Argument Long customerId, @Argument @Valid SalesRequestInput request) {
        return this.transactionService.getSales(customerId, request);
    }

    @MutationMapping
    public TransactionResult makePayment(@Argument @Valid TransactionInput transaction) {
        return this.transactionService.processTransaction(transaction);
    }
}
