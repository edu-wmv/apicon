package com.br.ufba.icon.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "deleted_points")
public class DeletedPointsEntity {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private String reason;
}
