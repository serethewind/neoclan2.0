package com.neoclan.transactionservice.service;

import com.neoclan.transactionservice.dto.*;
import com.neoclan.transactionservice.dto.communication.UserBalanceInfo;
import com.neoclan.transactionservice.dto.communication.UserInfo;
import com.neoclan.transactionservice.entity.TransactionEntity;
import com.neoclan.transactionservice.entity.TransactionType;
import com.neoclan.transactionservice.repository.TransactionRepository;
import com.neoclan.transactionservice.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private WebClient.Builder webClientBuilder;
//    private WebClient webClient;

    @Override
    public Response debitRequest(TransactionRequest transactionRequest) {
        //make a call to the IdentityManagementService via the fetchUserByAccountNumber controller to get the user performing the transaction
        //if balance is less than amount, money not sufficient
        //if balance is okay to make debit, subtract amount in the request dto from the balance
        //set new balance and save the user entity
        //call saveTransaction
        //return response saying successful
        UserInfo user = retrieveUser(transactionRequest.getAccountNumber());

        if (user == null) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null)
                    .build();
        }

        if (transactionRequest.getAmount().compareTo(user.getAccountBalance()) == 1) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_BALANCE_ENQUIRY)
                    .responseMessage(ResponseUtils.ACCOUNT_BALANCE_INSUFFICIENT)
                    .userData(null)
                    .build();
        }

        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.DEBIT)
                .accountNumber(user.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        UserBalanceInfo userBalanceInfo = UserBalanceInfo.builder()
                .accountNumber(user.getAccountNumber())
                .transactionAmount(transactionRequest.getAmount())
                .build();

        saveTransaction(transactionDto);

        //publish event that will be handled by the identity management service to update the user with the new user balance
        debitAndUpdateUserBalance(userBalanceInfo);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                .userData(UserData.builder()
                        .accountName(user.getAccountName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public Response creditRequest(TransactionRequest transactionRequest) {
        UserInfo user = retrieveUser(transactionRequest.getAccountNumber());

        if (user == null) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null)
                    .build();
        }

        user.setAccountBalance(user.getAccountBalance().add(transactionRequest.getAmount()));

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.CREDIT)
                .accountNumber(user.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        UserBalanceInfo userBalanceInfo = UserBalanceInfo.builder()
                .accountNumber(user.getAccountNumber())
                .transactionAmount(transactionRequest.getAmount())
                .build();

        saveTransaction(transactionDto);

        //publish event that will be handled by the identity management service to update the user with the new user balance
        creditAndUpdateUserBalance(userBalanceInfo);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .userData(UserData.builder()
                        .accountName(user.getAccountName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public Response transferRequest(TransferRequest transferRequest) {
        //get sending user and receiving user
        //call debitRequest method for sending user and creditRequest method for receiving user
        //based on whether the debitResponse from debitRequest method is successful, creditRequest will then be done
        //internally, the balance of each individual is updated and response i.e. alerts are sent to both users. Also, transaction is saved for both debit and credit.
        //this method just returns a customized response 'debited for transfer' to the user being debited.

        UserInfo sendingUser = retrieveUser(transferRequest.getSourceAccountNumber());
        UserInfo receivingUser = retrieveUser(transferRequest.getDestinationAccountNumber());

        TransactionRequest sendingTransactionRequest = new TransactionRequest(sendingUser.getAccountNumber(), transferRequest.getAmount());
        TransactionRequest receivingTransactionRequest = new TransactionRequest(receivingUser.getAccountNumber(), transferRequest.getAmount());

        Response debitResponse = debitRequest(sendingTransactionRequest);
        if (!debitResponse.getResponseCode().equalsIgnoreCase(ResponseUtils.SUCCESSFUL_TRANSACTION)) {
            return debitResponse;
        }
        creditRequest(receivingTransactionRequest);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED_FOR_TRANSFER)
                .userData(UserData.builder()
                        .accountName(sendingUser.getAccountName())
                        .accountBalance(debitResponse.getUserData().getAccountBalance())
                        .accountNumber(sendingUser.getAccountNumber())
                        .build())
                .build();
    }



    @Override
    public List<TransactionResponseDto> fetchTransactionByUser(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber).stream()
                .map(transaction -> TransactionResponseDto.builder()
                        .id(transaction.getTransactionId())
                        .transactionType(transaction.getTransactionType())
                        .accountNumber(transaction.getAccountNumber())
                        .amount(transaction.getAmount())
                        .timePerformed(transaction.getCreated())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> fetchCreditOrDebitTransactionByUser(String accountNumber, String debitOrCredit) {
        return transactionRepository.findByAccountNumber(accountNumber).stream().filter(transactionEntity -> String.valueOf(transactionEntity.getTransactionType()).equalsIgnoreCase(debitOrCredit)).map(transaction -> TransactionResponseDto.builder()
                .id(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .timePerformed(transaction.getCreated())
                .build()).collect(Collectors.toList());
    }

    private void saveTransaction(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .build();

        transactionRepository.save(transactionEntity);
    }

    private UserInfo retrieveUser(String accountNumber) {
//        WebClient.Builder() is preferable for load balancing where the service in communication with has multiple instances
        Response response = webClientBuilder.build().get()
                .uri("http://identity-management/api/v2/user/retrieve-accountName",
                        uriBuilder -> uriBuilder.queryParam("accountNumber", accountNumber).build())
                .retrieve()
                .bodyToMono(Response.class)
                .block();
//        Response response = webClient.get()
//                .uri("http://localhost:8081/api/v2/user/retrieve-accountName",
//                        uriBuilder -> uriBuilder.queryParam("accountNumber", accountNumber).build())
//                .retrieve()
//                .bodyToMono(Response.class)
//                .block();

        return UserInfo.builder()
                .accountName(response.getUserData().getAccountName())
                .accountBalance(response.getUserData().getAccountBalance())
                .accountNumber(response.getUserData().getAccountNumber())
                .build();
    }

    private void creditAndUpdateUserBalance(UserBalanceInfo userBalanceInfo) {
        webClientBuilder.build()
                .post()
                .uri("http://identity-management/api/v2/user/credit-and-update-accountBalance")
                .body(BodyInserters.fromValue(userBalanceInfo))
                .retrieve()
                .bodyToMono(Response.class)
                .block();
    }

    private void debitAndUpdateUserBalance(UserBalanceInfo userBalanceInfo) {
        webClientBuilder.build()
                .post()
                .uri("http://identity-management/api/v2/user/debit-and-update-accountBalance")
                .body(BodyInserters.fromValue(userBalanceInfo))
                .retrieve()
                .bodyToMono(Response.class)
                .block();
    }

}
