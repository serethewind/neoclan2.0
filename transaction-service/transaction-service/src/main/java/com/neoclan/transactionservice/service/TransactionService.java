package com.neoclan.transactionservice.service;

import com.neoclan.transactionservice.dto.*;

import java.util.List;

public interface TransactionService {


//    void saveTransaction (TransactionDto transactionDto);

    Response debitRequest(TransactionRequest transactionRequest);

    Response creditRequest(TransactionRequest transactionRequest);

    Response transferRequest(TransferRequest transferRequest);

    List<TransactionResponseDto> fetchTransactionByUser(String accountNumber);

    List<TransactionResponseDto> fetchCreditOrDebitTransactionByUser(String accountNumber, String debitOrCredit);
}
