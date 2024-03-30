package com.br.ufba.icon.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "iconicos")
public class IconicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String uid;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp hours = new Timestamp(10800000);

    private Boolean status = false;

    private String points_ids;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "points",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id")
    )

    private Set<PointEntity> points;
}
