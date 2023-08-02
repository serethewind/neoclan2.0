package com.neoclan.notificationservice.controller;

import com.neoclan.notificationservice.dto.EmailDetails;
import com.neoclan.notificationservice.dto.Response;
import com.neoclan.notificationservice.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("api/v2/email"))
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @GetMapping("testing")
    public ResponseEntity<String> checkTesting(){
        return ResponseEntity.ok("Testing api gateway");
    }

    @PostMapping("simpleMessage")
    public ResponseEntity<String> sendSimpleMessage(@RequestBody EmailDetails emailDetails){
        return ResponseEntity.ok(emailService.sendSimpleMessage(emailDetails));
    }

    @PostMapping("message")
    public ResponseEntity<String> sendMessageWithAttachment(@RequestBody EmailDetails emailDetails){
        return ResponseEntity.ok(emailService.sendMessageWithAttachment(emailDetails));
    }
}
