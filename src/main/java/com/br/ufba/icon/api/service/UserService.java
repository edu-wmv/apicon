package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.SignupRequest;
import com.br.ufba.icon.api.domain.User;
import com.br.ufba.icon.api.repository.UserRepositoryBase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepositoryBase repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryBase repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(@NonNull SignupRequest request) {
        String email = request.email();
        Optional<User> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateKeyException("User with the email address already in use");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.name(), request.email(), hashedPassword);
        repository.add(user);
    }
}
