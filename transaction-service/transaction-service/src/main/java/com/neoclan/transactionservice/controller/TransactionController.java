package com.neoclan.transactionservice.controller;

import com.neoclan.transactionservice.dto.Response;
import com.neoclan.transactionservice.dto.TransactionRequest;
import com.neoclan.transactionservice.dto.TransferRequest;
import com.neoclan.transactionservice.service.TransactionServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/transaction")
@AllArgsConstructor
public class TransactionController {

    private TransactionServiceImpl transactionService;

    @PutMapping("/debit")
    public ResponseEntity<Response> debitRequest(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.debitRequest(transactionRequest), HttpStatus.OK);
    }
    @PutMapping("/credit")
    public ResponseEntity<Response> creditRequest(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.creditRequest(transactionRequest), HttpStatus.OK);
    }
    @PutMapping("/debit")
    public ResponseEntity<Response> transferRequest(@RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(transactionService.transferRequest(transferRequest), HttpStatus.OK);
    }

}
