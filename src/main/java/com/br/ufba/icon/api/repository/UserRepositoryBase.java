package com.br.ufba.icon.api.repository;

import com.br.ufba.icon.api.domain.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;

@Repository
public class UserRepositoryBase {
    private final JdbcClient jdbcClient;

    public UserRepositoryBase(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void add(User user) {
        String INSERT = "INSERT INTO api_authentication.user (name, email, password) VALUES (:name, :email, :password)";
        long affected = jdbcClient.sql(INSERT)
                .param("name", user.name())
                .param("email", user.email())
                .param("password", user.password())
                .update();

        Assert.isTrue(affected == 1, "Could not add user");
    }

    public Optional<User> findByEmail(String email) {
        String FIND_BY_EMAIL = "SELECT * FROM api_authentication.user WHERE email = :email";
        return jdbcClient.sql(FIND_BY_EMAIL)
                .param("email", email)
                .query(User.class)
                .optional();
    }
}
