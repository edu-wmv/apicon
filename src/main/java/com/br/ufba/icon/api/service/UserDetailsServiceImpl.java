package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.domain.User;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.UserRepositoryBase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryBase repository;

    public UserDetailsServiceImpl(UserRepositoryBase repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format("User does not exist, email: %s", email)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.email())
                .password(user.password())
                .build();
    }
}
