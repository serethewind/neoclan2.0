package com.neoclan.identitymanagement.service;

import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserData;
import com.neoclan.identitymanagement.dto.UserUpdateRequestDto;
import com.neoclan.identitymanagement.entity.UserEntity;
import com.neoclan.identitymanagement.repository.UserRepository;
import com.neoclan.identitymanagement.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public List<Response> fetchAll() {
        return userRepository.findAll().stream().map(user -> Response.builder()
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build()).collect(Collectors.toList());
    }

    @Override
    public Response fetchSingleUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return Response.builder()
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response updateUserById(Long id, UserUpdateRequestDto userRequest) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        modelMapper.map(userRequest, user);

        userRepository.save(user);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public Response balanceEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);

        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null).
                    build();
        }

        UserEntity user = userRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new UsernameNotFoundException("User with account number not found"));

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(null)
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);

        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null).
                    build();
        }

        UserEntity user = userRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new UsernameNotFoundException("User with account number not found"));

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(null)
                        .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }
}
