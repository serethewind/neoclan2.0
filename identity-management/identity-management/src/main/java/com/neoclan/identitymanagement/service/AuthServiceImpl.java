package com.neoclan.identitymanagement.service;

import com.neoclan.identitymanagement.config.JwtService;
import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserData;
import com.neoclan.identitymanagement.dto.UserLoginRequestDto;
import com.neoclan.identitymanagement.dto.UserRegisterRequestDto;
import com.neoclan.identitymanagement.dto.communication.EmailDetails;
import com.neoclan.identitymanagement.dto.communication.EmailResponseDto;
import com.neoclan.identitymanagement.entity.RoleEntity;
import com.neoclan.identitymanagement.entity.TokenEntity;
import com.neoclan.identitymanagement.entity.TokenType;
import com.neoclan.identitymanagement.entity.UserEntity;
import com.neoclan.identitymanagement.repository.RoleRepository;
import com.neoclan.identitymanagement.repository.TokenRepository;
import com.neoclan.identitymanagement.repository.UserRepository;
import com.neoclan.identitymanagement.utils.ResponseUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;
    private JwtService jwtService;
    private WebClient webClientBuilder;

    @Override
    public Response loginUser(UserLoginRequestDto userLoginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication.getName());
        UserEntity user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        revokeValidTokens(user);
        TokenEntity tokenEntity = TokenEntity.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(tokenEntity);

        //Response utils.success and responseutils.user.login.success
        return Response.builder().responseCode(ResponseUtils.SUCCESS).responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS).userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .token(token)
                        .build())
                .build();
    }

    @Override
    public Response resetPassword(UserLoginRequestDto userLoginRequestDto) {
        UserEntity user = userRepository.findByUsername(userLoginRequestDto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setPassword(passwordEncoder.encode(userLoginRequestDto.getPassword()));
        userRepository.save(user);
        //Response utils.success and responseutils.user.password.changed
        return Response.builder().responseCode(ResponseUtils.SUCCESS).responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS).userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        boolean isExist = userRepository.existsByEmail(userRegisterRequestDto.getEmail());
        if (isExist) {
            return Response.builder().responseCode(ResponseUtils.USER_EXISTS_CODE).responseMessage(ResponseUtils.USER_EXISTS_MESSAGE).userData(null).build();
        } else {
            RoleEntity role = roleRepository.findByName("USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            UserEntity user = UserEntity.builder()
                    .firstName(userRegisterRequestDto.getFirstName())
                    .lastName(userRegisterRequestDto.getLastName())
                    .email(userRegisterRequestDto.getEmail())
                    .username(userRegisterRequestDto.getUsername())
                    .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                    .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.lengthOfAccountNumber))
                    .roles(Collections.singleton(role))
                    .status("ACTIVE")
                    .build();

            userRepository.save(user);

            String accountDetails = user.getFirstName() + user.getOtherName() + user.getLastName() + "\nAccount " + user.getAccountNumber();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(user.getEmail())
                    .subject("ACCOUNT DETAILS")
                    .message("Congratulations! Your account has been successfully created! Kindly find your details below: \n" + accountDetails)
                    .build();

//            String response = sendSimpleMail(emailDetails);
            //returns an object of string which says 'message successfully delivered'

            return Response.builder().responseCode(ResponseUtils.SUCCESS).responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS).userData(UserData.builder()
                            .accountBalance(user.getAccountBalance())
                            .accountNumber(user.getAccountNumber())
                            .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                            .build())
                    .build();
        }
    }

    private void revokeValidTokens(UserEntity users) {
        List<TokenEntity> tokenEntityList = tokenRepository.findAllValidTokensByUser(users.getId());
        if (tokenEntityList.isEmpty())
            return;
        tokenEntityList.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(tokenEntityList);
    }

    private String sendSimpleMail(EmailDetails emailDetails) {

//        EmailResponseDto response = webClientBuilder.build().post()
//                 .uri("http://localhost:8083/api/v2/email/simpleMessage")
//                .body(BodyInserters.fromValue(emailDetails))
//                .retrieve()
//                .bodyToMono(EmailResponseDto.class)
//                .block();


        String response = webClientBuilder.post()
                .uri("http://localhost:8083/api/v2/email/simpleMessage")
                .body(BodyInserters.fromValue(emailDetails))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

    private String sendSimpleMailWithAttachment(EmailDetails emailDetails) {

        String response = webClientBuilder.post()
                .uri("http://localhost:8083/api/v2/email/message")
                .body(BodyInserters.fromValue(emailDetails))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

}
