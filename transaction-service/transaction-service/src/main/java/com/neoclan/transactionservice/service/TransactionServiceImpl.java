package com.neoclan.transactionservice.service;

import com.neoclan.transactionservice.dto.Response;
import com.neoclan.transactionservice.dto.TransactionDto;
import com.neoclan.transactionservice.dto.TransactionRequest;
import com.neoclan.transactionservice.dto.TransferRequest;
import com.neoclan.transactionservice.entity.TransactionEntity;
import com.neoclan.transactionservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .build();

        transactionRepository.save(transactionEntity);
    }

    @Override
    public Response debitRequest(TransactionRequest transactionRequest) {
        return null;
        //make a call to the IdentityManagementService via the fetchUserByAccountNumber controller to get the user performing the transaction

    }

    @Override
    public Response creditRequest(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public Response transferRequest(TransferRequest transferRequest) {
        return null;
    }


}
