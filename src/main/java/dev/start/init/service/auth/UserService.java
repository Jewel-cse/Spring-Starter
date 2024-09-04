package dev.start.init.service;


import dev.start.init.dto.auth.SignupRequestDto;
import dev.start.init.entity.auth.User;

public interface UserService {

    User registerUser(SignupRequestDto userRegistrationDTO);

    User findUserByEmail(String email);

    void enableUser(String email);
}

