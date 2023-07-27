package com.neoclan.identitymanagement.controller;

import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserUpdateRequestDto;
import com.neoclan.identitymanagement.dto.communication.UserBalanceInfo;
import com.neoclan.identitymanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/user/")
@AllArgsConstructor
public class IdentityController {

    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateSingleUserDetails(@PathVariable("id") Long id, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
        return ResponseEntity.ok(userService.updateUserById(id, userUpdateRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<Response>> fetchAllUsers(){
        return ResponseEntity.ok(userService.fetchAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Response> fetchSingleUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.fetchSingleUserById(userId));
    }

    @GetMapping("retrieve-accountBalance")
    public ResponseEntity<Response> balanceEnquiry(@RequestParam("accountNumber") String accountNumber){
        return ResponseEntity.ok(userService.balanceEnquiry(accountNumber));
    }

    @GetMapping("retrieve-accountName")
    public ResponseEntity<Response> nameEnquiry(@RequestParam("accountNumber") String accountNumber){
        return ResponseEntity.ok(userService.nameEnquiry(accountNumber));
    }

    @PostMapping("credit-and-update-accountBalance")
    public ResponseEntity<Response> creditAndUpdateUserBalance(@RequestBody UserBalanceInfo userBalanceInfo){
        return ResponseEntity.ok(userService.UpdateUserBalanceAfterCredit(userBalanceInfo));
    }

    @PostMapping("debit-and-update-accountBalance")
    public ResponseEntity<Response> debitAndUpdateUserBalance(@RequestBody UserBalanceInfo userBalanceInfo){
        return ResponseEntity.ok(userService.UpdateUserBalanceAfterDebit(userBalanceInfo));
    }
}

