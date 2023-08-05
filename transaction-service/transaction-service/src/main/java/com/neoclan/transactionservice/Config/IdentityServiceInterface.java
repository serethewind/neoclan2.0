package com.neoclan.transactionservice.Config;

import com.neoclan.transactionservice.dto.Response;
import com.neoclan.transactionservice.dto.communication.UserBalanceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "IDENTITY-MANAGEMENT", path = "/api/v2/user/kyc")
public interface IdentityServiceInterface {

    @GetMapping("retrieve-accountName")
    public ResponseEntity<Response> nameEnquiry(@RequestParam("accountNumber") String accountNumber);

    @PostMapping("credit-and-update-accountBalance")
    public ResponseEntity<Response> creditAndUpdateUserBalance(@RequestBody UserBalanceInfo userBalanceInfo);

    @PostMapping("debit-and-update-accountBalance")
    public ResponseEntity<Response> debitAndUpdateUserBalance(@RequestBody UserBalanceInfo userBalanceInfo);

}


