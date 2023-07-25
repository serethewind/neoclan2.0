package com.neoclan.transactionservice.service;

import com.neoclan.transactionservice.dto.Response;
import com.neoclan.transactionservice.dto.TransactionDto;
import com.neoclan.transactionservice.dto.TransactionRequest;
import com.neoclan.transactionservice.dto.TransferRequest;

public interface TransactionService {


    void saveTransaction (TransactionDto transactionDto);

    Response debitRequest(TransactionRequest transactionRequest);

    Response creditRequest(TransactionRequest transactionRequest);

    Response transferRequest(TransferRequest transferRequest);
}
