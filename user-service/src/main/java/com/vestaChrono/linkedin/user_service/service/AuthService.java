package com.vestaChrono.linkedin.user_service.service;

import com.vestaChrono.linkedin.user_service.dto.LoginRequestDto;
import com.vestaChrono.linkedin.user_service.dto.SignUpRequestDto;
import com.vestaChrono.linkedin.user_service.dto.UserDto;
import com.vestaChrono.linkedin.user_service.entity.User;
import com.vestaChrono.linkedin.user_service.exception.BadRequestException;
import com.vestaChrono.linkedin.user_service.exception.ResourceNotFoundException;
import com.vestaChrono.linkedin.user_service.repository.UserRepository;
import com.vestaChrono.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
//        check if the user already exists
        boolean exist = userRepository.existsByEmail(signUpRequestDto.getEmail());
        if (exist) throw new BadRequestException("User already exists, can not signup");

//        convert to entity
        User user = modelMapper.map(signUpRequestDto, User.class);
//        hash the password
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

//        Save the user
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
//        get the user from userRepository
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

//        check if the password matches
        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            throw new BadRequestException("Incorrect Password");
        }

        return jwtService.generateAccessToken(user);
    }
}
