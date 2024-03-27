package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.SignupRequest;
import com.br.ufba.icon.api.domain.UserEntity;
import com.br.ufba.icon.api.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(@NonNull SignupRequest request) {
        String email = request.email();
        Optional<UserEntity> existingUser = repository.findByUsername(email);
        if (existingUser.isPresent()) {
            throw new DuplicateKeyException("User with the email address already in use");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        UserEntity user = new UserEntity();
        user.setUsername(request.name());
        user.setPassword(hashedPassword);
        user.setEmail(request.email());
        repository.save(user);
    }
}
