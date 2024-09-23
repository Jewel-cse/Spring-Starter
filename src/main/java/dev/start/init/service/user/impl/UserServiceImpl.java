package dev.start.init.service.auth.impl;


import dev.start.init.dto.user.PasswordResetRequestDto;
import dev.start.init.dto.user.SignupRequestDto;
import dev.start.init.mapper.auth.UserMapper;
import dev.start.init.entity.user.User;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public User registerUser(SignupRequestDto userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new RuntimeException("User already exists with this email.Please sign in");
        }

        User user = userMapper.toEntity(userRegistrationDTO);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setVerificationToken(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setPasswordResetToken(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public void enableUser(String email) {
        User user = findUserByEmail(email);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User resetPassword(String token, PasswordResetRequestDto resetPasswordRequestDto) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token."));

        user.setPassword(resetPasswordRequestDto.getNewPassword()); // You should also encode the password before saving
        user.setPasswordResetToken(null); // Clear the reset token
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token."));

        user.setEnabled(true); // Enable the user account
        user.setVerificationToken(null); // Clear the verification token
        userRepository.save(user);
    }
}

